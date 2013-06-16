package us.ihmc.darpaRoboticsChallenge.initialSetup;

import us.ihmc.SdfLoader.SDFRobot;
import us.ihmc.projectM.R2Sim02.initialSetup.RobotInitialSetup;

import javax.media.j3d.Transform3D;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

public class VRCTask1InVehicleHovering implements RobotInitialSetup<SDFRobot>
{

   private final double groundZ;

   public VRCTask1InVehicleHovering(double groundZ)
   {
      this.groundZ = groundZ;
   }

   public void initializeRobot(SDFRobot robot)
   {
      // Avoid singularities at startup
      robot.getOneDoFJoint("l_arm_usy").setQ(-1.0518);
      robot.getOneDoFJoint("l_arm_shx").setQ(-0.4177);
      robot.getOneDoFJoint("l_arm_ely").setQ(0.8856);
      robot.getOneDoFJoint("l_arm_elx").setQ(1.616);
      robot.getOneDoFJoint("l_arm_uwy").setQ(-0.4238);
      robot.getOneDoFJoint("l_arm_mwx").setQ(0.9266);
      
      robot.getOneDoFJoint("r_arm_usy").setQ(-0.297);
      robot.getOneDoFJoint("r_arm_shx").setQ(1.1054);
      robot.getOneDoFJoint("r_arm_ely").setQ(1.5975);
      robot.getOneDoFJoint("r_arm_elx").setQ(-1.1719);
      robot.getOneDoFJoint("r_arm_uwy").setQ(-0.5188);
      robot.getOneDoFJoint("r_arm_mwx").setQ(-0.8051);
      
      robot.getOneDoFJoint("l_leg_uhz").setQ(0.0405);
      robot.getOneDoFJoint("l_leg_mhx").setQ(0.0214);
      robot.getOneDoFJoint("l_leg_lhy").setQ(-1.1232);
      robot.getOneDoFJoint("l_leg_kny").setQ(1.2086);
      robot.getOneDoFJoint("l_leg_uay").setQ(0.0724);
      robot.getOneDoFJoint("l_leg_lax").setQ(-0.0005);

      robot.getOneDoFJoint("r_leg_uhz").setQ(-0.1527);
      robot.getOneDoFJoint("r_leg_mhx").setQ(-0.1526);
      robot.getOneDoFJoint("r_leg_lhy").setQ(-1.2337);
      robot.getOneDoFJoint("r_leg_kny").setQ(1.3334);
      robot.getOneDoFJoint("r_leg_uay").setQ(0.048);
      robot.getOneDoFJoint("r_leg_lax").setQ(0.1072);

      robot.getOneDoFJoint("back_lbz").setQ(-0.1519);
      robot.getOneDoFJoint("back_mby").setQ(0.164);
      robot.getOneDoFJoint("back_ubx").setQ(-0.0049);

      robot.getOneDoFJoint("left_f0_j0").setQ(0.1098);
      robot.getOneDoFJoint("left_f1_j0").setQ(0.1060);
      robot.getOneDoFJoint("left_f2_j0").setQ(0.1040);
      robot.getOneDoFJoint("left_f3_j0").setQ(-0.0376);

      robot.getOneDoFJoint("left_f0_j1").setQ(1.1659);
      robot.getOneDoFJoint("left_f1_j1").setQ(1.1627);
      robot.getOneDoFJoint("left_f2_j1").setQ(1.1598);
      robot.getOneDoFJoint("left_f3_j1").setQ(1.0352);
      
      robot.getOneDoFJoint("left_f0_j2").setQ(1.0046);
      robot.getOneDoFJoint("left_f1_j2").setQ(1.0054);
      robot.getOneDoFJoint("left_f2_j2").setQ(1.0060);
      robot.getOneDoFJoint("left_f3_j2").setQ(0.0231);

      robot.getOneDoFJoint("right_f3_j1").setQ(-1.57);

      robot.setPositionInWorld(new Vector3d(-0.096, 0.3726, 1.1064));
      robot.setOrientation(new Quat4d(-0.0076, -0.0814, 0.0747, 0.9938));
   }
}