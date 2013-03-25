package us.ihmc.commonWalkingControlModules.stateEstimation.measurementModelElements;



import java.util.Random;

import javax.vecmath.Vector3d;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.EjmlUnitTests;
import org.junit.Test;

import us.ihmc.commonWalkingControlModules.stateEstimation.CenterOfMassBasedFullRobotModelUpdater;
import us.ihmc.controlFlow.ControlFlowElement;
import us.ihmc.controlFlow.ControlFlowInputPort;
import us.ihmc.controlFlow.ControlFlowOutputPort;
import us.ihmc.controlFlow.NullControlFlowElement;
import us.ihmc.utilities.RandomTools;
import us.ihmc.utilities.math.geometry.FrameOrientation;
import us.ihmc.utilities.math.geometry.FramePoint;
import us.ihmc.utilities.math.geometry.FrameVector;
import us.ihmc.utilities.math.geometry.ReferenceFrame;
import us.ihmc.utilities.screwTheory.CenterOfMassCalculator;
import us.ihmc.utilities.screwTheory.CenterOfMassJacobian;
import us.ihmc.utilities.screwTheory.RigidBody;
import us.ihmc.utilities.screwTheory.ScrewTestTools.RandomFloatingChain;
import us.ihmc.utilities.screwTheory.SixDoFJoint;
import us.ihmc.utilities.screwTheory.Twist;
import us.ihmc.utilities.screwTheory.TwistCalculator;

import com.yobotics.simulationconstructionset.YoVariableRegistry;

public class PointVelocityMeasurementModelElementTest
{
   private static final Vector3d X = new Vector3d(1.0, 0.0, 0.0);
   private static final Vector3d Y = new Vector3d(0.0, 1.0, 0.0);
   private static final Vector3d Z = new Vector3d(0.0, 0.0, 1.0);

   @Test
   public void test()
   {
      Random random = new Random(1235L);
      Vector3d[] jointAxes = new Vector3d[] {X, Y, Z};
      RandomFloatingChain randomFloatingChain = new RandomFloatingChain(random, jointAxes);
      RigidBody elevator = randomFloatingChain.getElevator();
      SixDoFJoint rootJoint = randomFloatingChain.getRootJoint();

      RigidBody estimationLink = randomFloatingChain.getRootJoint().getSuccessor();
      ReferenceFrame estimationFrame = randomFloatingChain.getRootJoint().getFrameAfterJoint();
      RigidBody measurementLink = randomFloatingChain.getRevoluteJoints().get(jointAxes.length - 1).getSuccessor();
      ReferenceFrame measurementFrame = measurementLink.getBodyFixedFrame();

      ControlFlowElement controlFlowElement = new NullControlFlowElement();

      TwistCalculator twistCalculator = new TwistCalculator(elevator.getBodyFixedFrame(), randomFloatingChain.getElevator());
      CenterOfMassCalculator centerOfMassCalculator = new CenterOfMassCalculator(elevator, ReferenceFrame.getWorldFrame());
      CenterOfMassJacobian centerOfMassJacobian = new CenterOfMassJacobian(elevator);

      String name = "test";
      YoVariableRegistry registry = new YoVariableRegistry(name);

      ControlFlowInputPort<FrameVector> pointVelocityMeasurementInputPort = new ControlFlowInputPort<FrameVector>(controlFlowElement);

      ControlFlowOutputPort<FramePoint> centerOfMassPositionPort = new ControlFlowOutputPort<FramePoint>(controlFlowElement);
      ControlFlowOutputPort<FrameVector> centerOfMassVelocityPort = new ControlFlowOutputPort<FrameVector>(controlFlowElement);
      ControlFlowOutputPort<FrameOrientation> orientationPort = new ControlFlowOutputPort<FrameOrientation>(controlFlowElement);
      ControlFlowOutputPort<FrameVector> angularVelocityPort = new ControlFlowOutputPort<FrameVector>(controlFlowElement);
      RigidBody stationaryPointLink = measurementLink;
      FramePoint stationaryPoint = new FramePoint(measurementFrame, RandomTools.generateRandomPoint(random, 1.0, 1.0, 1.0));
      PointVelocityMeasurementModelElement modelElement = new PointVelocityMeasurementModelElement(name, pointVelocityMeasurementInputPort,
                                                             centerOfMassPositionPort, centerOfMassVelocityPort, orientationPort, angularVelocityPort,
                                                             estimationFrame, stationaryPointLink, stationaryPoint, twistCalculator, registry);

      randomFloatingChain.setRandomPositionsAndVelocities(random);
      twistCalculator.compute();

      setCenterOfMassToActual(centerOfMassCalculator, centerOfMassPositionPort);
      setCenterOfMassVelocityToActual(centerOfMassJacobian, centerOfMassVelocityPort);
      setOrientationToActual(estimationFrame, orientationPort);
      setAngularVelocityToActual(estimationLink, estimationFrame, twistCalculator, angularVelocityPort);
      setMeasuredPointVelocityToActual(twistCalculator, stationaryPointLink, stationaryPoint, pointVelocityMeasurementInputPort);

      DenseMatrix64F zeroResidual = modelElement.computeResidual();
      DenseMatrix64F zeroVector = new DenseMatrix64F(3, 1);
      EjmlUnitTests.assertEquals(zeroVector, zeroResidual, 1e-12);

      double perturbation = 1e-6;
      double tol = 1e-12;
      Runnable updater = new CenterOfMassBasedFullRobotModelUpdater(twistCalculator, centerOfMassPositionPort, centerOfMassVelocityPort, orientationPort,
                            angularVelocityPort, estimationLink, estimationFrame, rootJoint);
      modelElement.computeMatrixBlocks();

      // CoM velocity perturbations
      MeasurementModelTestTools.assertOutputMatrixCorrectUsingPerturbation(centerOfMassVelocityPort, modelElement,
              new FrameVector(centerOfMassVelocityPort.getData()), perturbation, tol, updater);

      // angular velocity perturbations
      MeasurementModelTestTools.assertOutputMatrixCorrectUsingPerturbation(angularVelocityPort, modelElement, new FrameVector(angularVelocityPort.getData()),
              perturbation, tol, updater);
      
      // orientation perturbations
      MeasurementModelTestTools.assertOutputMatrixCorrectUsingPerturbation(orientationPort, modelElement, new FrameOrientation(orientationPort.getData()),
            perturbation, tol, updater);
   }

   private void setAngularVelocityToActual(RigidBody estimationLink, ReferenceFrame estimationFrame, TwistCalculator twistCalculator,
           ControlFlowOutputPort<FrameVector> angularVelocityPort)
   {
      Twist estimationLinkTwist = new Twist();
      twistCalculator.packTwistOfBody(estimationLinkTwist, estimationLink);
      FrameVector angularVelocity = new FrameVector(estimationLinkTwist.getExpressedInFrame());
      estimationLinkTwist.packAngularPart(angularVelocity);
      angularVelocity.changeFrame(estimationFrame);
      angularVelocityPort.setData(angularVelocity);
   }

   private void setOrientationToActual(ReferenceFrame estimationFrame, ControlFlowOutputPort<FrameOrientation> orientationPort)
   {
      FrameOrientation orientation = new FrameOrientation(estimationFrame);
      orientation.changeFrame(ReferenceFrame.getWorldFrame());
      orientationPort.setData(orientation);
   }

   private void setCenterOfMassVelocityToActual(CenterOfMassJacobian centerOfMassJacobian, ControlFlowOutputPort<FrameVector> centerOfMassVelocityPort)
   {
      centerOfMassJacobian.compute();
      FrameVector centerOfMassVelocity = new FrameVector(ReferenceFrame.getWorldFrame());
      centerOfMassJacobian.packCenterOfMassVelocity(centerOfMassVelocity);
      centerOfMassVelocityPort.setData(centerOfMassVelocity);
   }

   private void setCenterOfMassToActual(CenterOfMassCalculator centerOfMassCalculator, ControlFlowOutputPort<FramePoint> centerOfMassPositionPort)
   {
      centerOfMassCalculator.compute();
      FramePoint centerOfMass = centerOfMassCalculator.getCenterOfMass();
      centerOfMassPositionPort.setData(centerOfMass);
   }

   private void setMeasuredPointVelocityToActual(TwistCalculator twistCalculator, RigidBody stationaryPointLink, FramePoint point,
           ControlFlowInputPort<FrameVector> pointVelocityMeasurementInputPort)
   {
      Twist twist = new Twist();
      twistCalculator.packTwistOfBody(twist, stationaryPointLink);
      twist.changeFrame(twist.getBaseFrame());
      point.changeFrame(twist.getBaseFrame());
      FrameVector pointVelocity = new FrameVector(twist.getBaseFrame());
      twist.packVelocityOfPointFixedInBodyFrame(pointVelocity, point);
      pointVelocity.changeFrame(ReferenceFrame.getWorldFrame());
      pointVelocityMeasurementInputPort.setData(pointVelocity);
   }
}
