package us.ihmc.sensorProcessing.stateEstimation.measurmentModelElements;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;

import us.ihmc.controlFlow.ControlFlowInputPort;
import us.ihmc.controlFlow.ControlFlowOutputPort;
import us.ihmc.sensorProcessing.stateEstimation.evaluation.FullInverseDynamicsStructure;
import us.ihmc.sensorProcessing.stateEstimation.evaluation.RigidBodyToIndexMap;
import us.ihmc.sensorProcessing.stateEstimation.sensorConfiguration.PointVelocityDataObject;
import us.ihmc.utilities.math.geometry.FrameOrientation;
import us.ihmc.utilities.math.geometry.FramePoint;
import us.ihmc.utilities.math.geometry.FrameVector;
import us.ihmc.utilities.math.geometry.ReferenceFrame;
import us.ihmc.utilities.screwTheory.AfterJointReferenceFrameNameMap;

import com.yobotics.simulationconstructionset.YoVariableRegistry;

/**
 * @author twan
 *         Date: 4/27/13
 */
public class AggregatePointVelocityMeasurementModelElement implements MeasurementModelElement
{
   private YoVariableRegistry registry = new YoVariableRegistry(getClass().getSimpleName());
   private final List<PointVelocityMeasurementModelElement> elementPool = new ArrayList<PointVelocityMeasurementModelElement>();
   private final ControlFlowInputPort<Set<PointVelocityDataObject>> inputPort;
   private final ControlFlowOutputPort<FramePoint> centerOfMassPositionPort;
   private final ControlFlowOutputPort<FrameVector> centerOfMassVelocityPort;
   private final ControlFlowOutputPort<FrameOrientation> orientationPort;
   private final ControlFlowOutputPort<FrameVector> angularVelocityPort;


   private final ControlFlowInputPort<FullInverseDynamicsStructure> inverseDynamicsStructureInputPort;
   private final AfterJointReferenceFrameNameMap referenceFrameNameMap;
   private final RigidBodyToIndexMap rigidBodyToIndexMap;

   private final ReferenceFrame estimationFrame;
   private final Set<ControlFlowOutputPort<?>> statePorts = new LinkedHashSet<ControlFlowOutputPort<?>>();

   private final Map<ControlFlowOutputPort<?>, DenseMatrix64F> outputMatrixBlocks = new LinkedHashMap<ControlFlowOutputPort<?>, DenseMatrix64F>();
   private final DenseMatrix64F measurementCovarianceMatrixBlock = new DenseMatrix64F(1, 1);
   private final DenseMatrix64F residual = new DenseMatrix64F(1, 1);
   private final DenseMatrix64F singlePointCovariance = new DenseMatrix64F(PointVelocityMeasurementModelElement.SIZE,
                                                           PointVelocityMeasurementModelElement.SIZE);

   private int nElementsInUse;


   public AggregatePointVelocityMeasurementModelElement(ControlFlowInputPort<Set<PointVelocityDataObject>> inputPort,
         ControlFlowOutputPort<FramePoint> centerOfMassPositionPort, ControlFlowOutputPort<FrameVector> centerOfMassVelocityPort,
         ControlFlowOutputPort<FrameOrientation> orientationPort, ControlFlowOutputPort<FrameVector> angularVelocityPort,
         ControlFlowInputPort<FullInverseDynamicsStructure> inverseDynamicsStructureInputPort, AfterJointReferenceFrameNameMap referenceFrameNameMap,
         RigidBodyToIndexMap rigidBodyToIndexMap, ReferenceFrame estimationFrame)
   {
      this.inputPort = inputPort;
      this.centerOfMassPositionPort = centerOfMassPositionPort;
      this.centerOfMassVelocityPort = centerOfMassVelocityPort;
      this.orientationPort = orientationPort;
      this.angularVelocityPort = angularVelocityPort;
      this.inverseDynamicsStructureInputPort = inverseDynamicsStructureInputPort;
      this.referenceFrameNameMap = referenceFrameNameMap;
      this.rigidBodyToIndexMap = rigidBodyToIndexMap;
      this.estimationFrame = estimationFrame;

      statePorts.add(centerOfMassPositionPort);
      statePorts.add(centerOfMassVelocityPort);
      statePorts.add(orientationPort);
      statePorts.add(angularVelocityPort);

      for (ControlFlowOutputPort<?> statePort : statePorts)
      {
         outputMatrixBlocks.put(statePort, new DenseMatrix64F(1, 1));
      }
   }

   public void computeMatrixBlocks()
   {
      Set<PointVelocityDataObject> pointVelocityDataObjects = inputPort.getData();
      nElementsInUse = pointVelocityDataObjects.size();

      for (int i = elementPool.size(); i < nElementsInUse; i++)
      {
         String name = "pointVelocity" + i;
         ControlFlowInputPort<PointVelocityDataObject> pointVelocityMeasurementInputPort = new ControlFlowInputPort<PointVelocityDataObject>("pointVelocityMeasurementInputPort", null);

         PointVelocityMeasurementModelElement element = new PointVelocityMeasurementModelElement(name, pointVelocityMeasurementInputPort,
                                                           centerOfMassPositionPort, centerOfMassVelocityPort, orientationPort, angularVelocityPort,
                                                           estimationFrame, inverseDynamicsStructureInputPort, referenceFrameNameMap, rigidBodyToIndexMap, registry);

         element.setNoiseCovariance(singlePointCovariance);
         elementPool.add(element);
      }

      reshapeAndZeroMatrices();

      int rowIndex = 0;
      int elementIndex = 0;
      for (PointVelocityDataObject pointVelocityDataObject : pointVelocityDataObjects)
      {
         PointVelocityMeasurementModelElement element = elementPool.get(elementIndex);
         element.getPointVelocityMeasurementInputPort().setData(pointVelocityDataObject);
         element.computeMatrixBlocks();

         for (ControlFlowOutputPort<?> statePort : statePorts)
         {
            DenseMatrix64F outputMatrixBlock = element.getOutputMatrixBlock(statePort);
            DenseMatrix64F stateOutputMatrixBlock = outputMatrixBlocks.get(statePort);
            CommonOps.insert(outputMatrixBlock, stateOutputMatrixBlock, rowIndex, 0);
         }

         CommonOps.insert(element.getMeasurementCovarianceMatrixBlock(), measurementCovarianceMatrixBlock, rowIndex, rowIndex);

         rowIndex += PointVelocityMeasurementModelElement.SIZE;
         elementIndex++;
      }
   }

   private void reshapeAndZeroMatrices()
   {
      int size = nElementsInUse * PointVelocityMeasurementModelElement.SIZE;
      for (ControlFlowOutputPort<?> statePort : statePorts)
      {
         DenseMatrix64F outputMatrixBlock = outputMatrixBlocks.get(statePort);
         outputMatrixBlock.reshape(size, PointVelocityMeasurementModelElement.SIZE);
         outputMatrixBlock.zero();
      }

      measurementCovarianceMatrixBlock.reshape(size, size);
      measurementCovarianceMatrixBlock.zero();
      residual.reshape(size, 1);
      residual.zero();
   }

   public DenseMatrix64F getOutputMatrixBlock(ControlFlowOutputPort<?> statePort)
   {
      return outputMatrixBlocks.get(statePort);
   }

   public DenseMatrix64F getMeasurementCovarianceMatrixBlock()
   {
      return measurementCovarianceMatrixBlock;
   }

   public DenseMatrix64F computeResidual()
   {
      int rowStart = 0;
      for (int i = 0; i < nElementsInUse; i++)
      {
         PointVelocityMeasurementModelElement element = elementPool.get(i);
         DenseMatrix64F residualBlock = element.computeResidual();
         CommonOps.insert(residualBlock, residual, rowStart, 0);
         rowStart += PointVelocityMeasurementModelElement.SIZE;
      }

      return residual;
   }

   public Set<ControlFlowOutputPort<?>> getStatePorts()
   {
      return statePorts;
   }

   public void setNoiseCovariance(DenseMatrix64F covariance)
   {
      singlePointCovariance.set(covariance);

      for (PointVelocityMeasurementModelElement pointVelocityMeasurementModelElement : elementPool)
      {
         pointVelocityMeasurementModelElement.setNoiseCovariance(covariance);
      }
   }
}
