package us.ihmc.acsell;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.media.j3d.Transform3D;
import javax.vecmath.Vector3d;

import us.ihmc.SdfLoader.SDFJointNameMap.JointRole;
import us.ihmc.commonWalkingControlModules.partNamesAndTorques.ArmJointName;
import us.ihmc.commonWalkingControlModules.partNamesAndTorques.LegJointName;
import us.ihmc.commonWalkingControlModules.partNamesAndTorques.LimbName;
import us.ihmc.commonWalkingControlModules.partNamesAndTorques.NeckJointName;
import us.ihmc.commonWalkingControlModules.partNamesAndTorques.SpineJointName;
import us.ihmc.darpaRoboticsChallenge.DRCRobotModel;
import us.ihmc.darpaRoboticsChallenge.drcRobot.DRCRobotContactPointParamaters;
import us.ihmc.darpaRoboticsChallenge.drcRobot.DRCRobotJointMap;
import us.ihmc.robotSide.RobotSide;
import us.ihmc.robotSide.SideDependentList;
import us.ihmc.utilities.Pair;

/**
 * Created by dstephen on 2/14/14.
 */
public class BonoJointMap extends DRCRobotJointMap
{
   public static final String chestName = "utorso";
   public static final String pelvisName = "pelvis";
   public static final String headName = null;
   public static final String lidarSensorName = null;
   public static final String leftCameraName = null;
   public static final String rightCameraName = null;
   public static final String imuSensor = pelvisName + "_pelvisIMU";
   public static final String[] imuSensorsToUse = {imuSensor};
   
   private final SpineJointName[] spineJoints = { SpineJointName.SPINE_ROLL, SpineJointName.SPINE_PITCH, SpineJointName.SPINE_YAW };
   private final LegJointName[] legJoints = { LegJointName.HIP_ROLL, LegJointName.HIP_YAW, LegJointName.HIP_PITCH, LegJointName.KNEE, LegJointName.ANKLE_ROLL,
         LegJointName.ANKLE_PITCH };
   
   protected final LinkedHashMap<String, JointRole> jointRoles = new LinkedHashMap<>();
   private final LinkedHashMap<String, Pair<RobotSide, LegJointName>> legJointNames = new LinkedHashMap<String, Pair<RobotSide, LegJointName>>();
   private final LinkedHashMap<String, SpineJointName> spineJointNames = new LinkedHashMap<>();
   private final SideDependentList<String> jointBeforeFeetNames = new SideDependentList<>();
   private final SideDependentList<String> feetForceSensorNames = new SideDependentList<String>();
   private final LinkedHashMap<String, Pair<RobotSide, LimbName>> limbNames = new LinkedHashMap<>();
   private final LinkedHashMap<String, Pair<RobotSide, ArmJointName>> armJointNames = new LinkedHashMap<>();
   private final LinkedHashMap<String, NeckJointName> neckJointNames = new LinkedHashMap<>();
   private final BonoRobotModel robotModel;
   private final DRCRobotContactPointParamaters contactPointParamaters;
   private final String[] forceSensorNames;
   private final NeckJointName[] neckJoints = {};
   private final ArmJointName[] armJoints = {};
   
   public BonoJointMap(BonoRobotModel robotModel)
   {
      super();
      this.robotModel = robotModel;
      for (RobotSide robotSide : RobotSide.values())
      {
         String robotSideLowerCaseFirstLetter = robotSide.getSideNameFirstLetter().toLowerCase();
         legJointNames.put(robotSideLowerCaseFirstLetter + "_leg_mhx", new Pair<>(robotSide, LegJointName.HIP_ROLL));
         legJointNames.put(robotSideLowerCaseFirstLetter + "_leg_uhz", new Pair<>(robotSide, LegJointName.HIP_YAW));
         legJointNames.put(robotSideLowerCaseFirstLetter + "_leg_lhy", new Pair<>(robotSide, LegJointName.HIP_PITCH));
         legJointNames.put(robotSideLowerCaseFirstLetter + "_leg_kny", new Pair<>(robotSide, LegJointName.KNEE));
         legJointNames.put(robotSideLowerCaseFirstLetter + "_leg_lax", new Pair<>(robotSide, LegJointName.ANKLE_ROLL));
         legJointNames.put(robotSideLowerCaseFirstLetter + "_leg_uay", new Pair<>(robotSide, LegJointName.ANKLE_PITCH));
         limbNames.put(null, new Pair<>(robotSide, LimbName.ARM));
         limbNames.put(robotSideLowerCaseFirstLetter + "_foot", new Pair<>(robotSide, LimbName.LEG));
         jointBeforeFeetNames.put(robotSide, robotSideLowerCaseFirstLetter + "_leg_lax");
         feetForceSensorNames.put(robotSide, jointBeforeFeetNames.get(robotSide));
      }     
      spineJointNames.put("back_lbx", SpineJointName.SPINE_ROLL);
      spineJointNames.put("back_mby", SpineJointName.SPINE_PITCH);
      spineJointNames.put("back_ubz", SpineJointName.SPINE_YAW);
      forceSensorNames= new String[]{feetForceSensorNames.get(RobotSide.LEFT), feetForceSensorNames.get(RobotSide.RIGHT)};

      for (String spineJoint : spineJointNames.keySet())
      {
         jointRoles.put(spineJoint, JointRole.SPINE);
      }
      for (String legJoint : legJointNames.keySet())
      {
         jointRoles.put(legJoint, JointRole.LEG);
      }
      contactPointParamaters = new ACSELLContactPointParamaters(this);
   }

   @Override
   public String getModelName()
   {
      return "bono";
   }

   @Override
   public double getAnkleHeight()
   {
      return BonoPhysicalProperties.ankleHeight;
   }

   @Override
   public SideDependentList<Transform3D> getAnkleToSoleFrameTransform()
   {
      return BonoPhysicalProperties.ankleToSoleFrameTransforms;
   }

   @Override
   public DRCRobotModel getSelectedModel()
   {
      return robotModel;
   }

   @Override
   public String[] getOrderedJointNames()
   {
      return BonoOrderedJointNames.jointNames;
   }
   
   @Override
   public SpineJointName[] getSpineJointNames()
   {
      return spineJoints;
   }
   
   @Override
   public List<Pair<String, Vector3d>> getJointNameGroundContactPointMap()
   {
      return contactPointParamaters.getJointNameGroundContactPointMap();
   }
   
   @Override
   public double getPelvisToFoot()
   {
      return AxlPhysicalProperties.pelvisToFoot;
   }

   @Override
   public String getLidarJointName()
   {
      return null;
   }

   @Override
   public JointRole getJointRole(String jointName)
   {
      return jointRoles.get(jointName);
   }

   @Override
   public Pair<RobotSide, LegJointName> getLegJointName(String jointName)
   {
      return legJointNames.get(jointName);
   }

   @Override
   public Pair<RobotSide, ArmJointName> getArmJointName(String jointName)
   {
      return armJointNames.get(jointName);
   }

   @Override
   public NeckJointName getNeckJointName(String jointName)
   {
      return neckJointNames.get(jointName);
   }

   @Override
   public SpineJointName getSpineJointName(String jointName)
   {
      return spineJointNames.get(jointName);
   }

   @Override
   public Pair<RobotSide, LimbName> getLimbName(String limbName)
   {
      return limbNames.get(limbName);
   }

   @Override
   public String getPelvisName()
   {
      return pelvisName;
   }

   @Override
   public String getChestName()
   {
      return chestName;
   }

   @Override
   public String getHeadName()
   {
      return headName;
   }

   @Override
   public boolean isTorqueVelocityLimitsEnabled()
   {
      return false;
   }

   @Override
   public Set<String> getLastSimulatedJoints()
   {
      HashSet<String> lastSimulatedJoints = new HashSet<>();

      // don't simulate children of ll_ankle_pulley, lr_ankle_pulley, rr_ankle_pulley and rl_ankle_pulley

      for(RobotSide robotSide : RobotSide.values())
      {
         String prefix = robotSide.getSideNameFirstLetter().toLowerCase();
         lastSimulatedJoints.add(prefix + "l_ankle_pulley");
         lastSimulatedJoints.add(prefix + "r_ankle_pulley");
      }

      return lastSimulatedJoints;
   }

   @Override
   public String[] getIMUSensorsToUse()
   {
      return imuSensorsToUse;
   }

   @Override
   public String getLidarSensorName()
   {
      return lidarSensorName;
   }

   @Override
   public String getLeftCameraName()
   {
      return leftCameraName;
   }

   public String getRightCameraName()
   {
      return rightCameraName;
   }

   @Override
   public String getJointBeforeFootName(RobotSide robotSide)
   {
      return jointBeforeFeetNames.get(robotSide);
   }

   @Override
   public LegJointName[] getLegJointNames()
   {
      return legJoints;
   }

   @Override
   public ArmJointName[] getArmJointNames()
   {
      return armJoints;
   }

   @Override
   public NeckJointName[] getNeckJointNames()
   {
      return neckJoints;
   }

   @Override
   public String[] getForceSensorNames()
   {
      return forceSensorNames;
   }

   @Override
   public String getNameOfJointBeforeChest()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String getNameOfJointBeforeThigh(RobotSide robotSide)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String getNameOfJointBeforeHand(RobotSide robotSide)
   {
      return null;
   }

   @Override
   public SideDependentList<String> getFeetForceSensorNames()
   {
      return feetForceSensorNames;
   }

   @Override
   public SideDependentList<String> getJointBeforeThighNames()
   {
      return AxlOrderedJointNames.getJointBeforeThighNames();
   }

   protected LinkedHashMap<String, Pair<RobotSide, LegJointName>> getLegJointNamesMap()
   {
      return legJointNames;
   }

   @Override
   public String getHighestNeckPitchJointName()
   {
      return null;
   }
}
