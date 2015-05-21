package us.ihmc.wanderer.hardware.sensorReader;

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;

import us.ihmc.acsell.CostOfTransportCalculator;
import us.ihmc.acsell.hardware.sensorReader.AcsellSensorReader;
import us.ihmc.darpaRoboticsChallenge.drcRobot.DRCRobotModel;
import us.ihmc.sensorProcessing.parameters.DRCRobotSensorInformation;
import us.ihmc.sensorProcessing.sensors.RawJointSensorDataHolderMap;
import us.ihmc.sensorProcessing.simulatedSensors.SensorReader;
import us.ihmc.sensorProcessing.simulatedSensors.SensorReaderFactory;
import us.ihmc.sensorProcessing.simulatedSensors.StateEstimatorSensorDefinitions;
import us.ihmc.sensorProcessing.stateEstimation.StateEstimatorParameters;
import us.ihmc.utilities.IMUDefinition;
import us.ihmc.utilities.humanoidRobot.model.ContactSensorHolder;
import us.ihmc.utilities.humanoidRobot.model.DesiredJointDataHolder;
import us.ihmc.utilities.humanoidRobot.model.ForceSensorDefinition;
import us.ihmc.utilities.screwTheory.InverseDynamicsJoint;
import us.ihmc.utilities.screwTheory.OneDoFJoint;
import us.ihmc.utilities.screwTheory.ScrewTools;
import us.ihmc.utilities.screwTheory.SixDoFJoint;
import us.ihmc.wanderer.hardware.WandererJoint;
import us.ihmc.wanderer.hardware.WandererUtil;
import us.ihmc.wanderer.hardware.state.WandererState;
import us.ihmc.yoUtilities.dataStructure.registry.YoVariableRegistry;

public class WandererSensorReaderFactory implements SensorReaderFactory
{
   private StateEstimatorSensorDefinitions stateEstimatorSensorDefinitions;
   private AcsellSensorReader<WandererJoint> sensorReader;
   
   private final CostOfTransportCalculator costOfTransportCalculator;

   private final DRCRobotSensorInformation sensorInformation;
   private final StateEstimatorParameters stateEstimatorParameters;
   
   public WandererSensorReaderFactory(DRCRobotModel robotModel, CostOfTransportCalculator costOfTransportCalculator)
   {
      sensorInformation = robotModel.getSensorInformation();
      stateEstimatorParameters = robotModel.getStateEstimatorParameters();
      this.costOfTransportCalculator = costOfTransportCalculator;
   }

   @Override
   public void build(SixDoFJoint rootJoint, IMUDefinition[] imuDefinitions, ForceSensorDefinition[] forceSensorDefinitions,
         ContactSensorHolder contactSensorHolder, RawJointSensorDataHolderMap rawJointSensorDataHolderMap, DesiredJointDataHolder estimatorDesiredJointDataHolder, YoVariableRegistry parentRegistry)
   {
      stateEstimatorSensorDefinitions = new StateEstimatorSensorDefinitions();

      for (InverseDynamicsJoint joint : ScrewTools.computeSubtreeJoints(rootJoint.getSuccessor()))
      {
         if (joint instanceof OneDoFJoint)
         {
            OneDoFJoint oneDoFJoint = (OneDoFJoint) joint;
            stateEstimatorSensorDefinitions.addJointSensorDefinition(oneDoFJoint);
         }
      }

      for (IMUDefinition imuDefinition : imuDefinitions)
      {
         stateEstimatorSensorDefinitions.addIMUSensorDefinition(imuDefinition);
      }

      for (ForceSensorDefinition forceSensorDefinition : forceSensorDefinitions)
      {
         stateEstimatorSensorDefinitions.addForceSensorDefinition(forceSensorDefinition);
      }

      YoVariableRegistry sensorReaderRegistry = new YoVariableRegistry("WandererSensorReader");
      WandererState state = new WandererState(stateEstimatorParameters.getEstimatorDT(), sensorReaderRegistry);
      List<OneDoFJoint> jointList = stateEstimatorSensorDefinitions.getJointSensorDefinitions();
      EnumMap<WandererJoint, OneDoFJoint> wandererJoints = WandererUtil.createJointMap(jointList);
      sensorReader = new AcsellSensorReader<WandererJoint>(state, WandererJoint.values, wandererJoints, stateEstimatorSensorDefinitions, rawJointSensorDataHolderMap, sensorInformation, stateEstimatorParameters,
            estimatorDesiredJointDataHolder, sensorReaderRegistry);
      
      
      costOfTransportCalculator.setTotalWorkVariable(state.getPowerDistributionState().getTotalWorkVariable());
      parentRegistry.addChild(sensorReaderRegistry);

   }

   @Override
   public SensorReader getSensorReader()
   {
      return sensorReader;
   }

   @Override
   public StateEstimatorSensorDefinitions getStateEstimatorSensorDefinitions()
   {
      return stateEstimatorSensorDefinitions;
   }

   public void connect() throws IOException
   {
      sensorReader.connect();
   }

   public void disconnect()
   {
      sensorReader.disconnect();
   }

   @Override
   public boolean useStateEstimator()
   {
      return true;
   }

}