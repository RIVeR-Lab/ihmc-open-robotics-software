package us.ihmc.atlas.initialSetup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

import us.ihmc.SdfLoader.SDFRobot;
import us.ihmc.atlas.AtlasJointMap;
import us.ihmc.darpaRoboticsChallenge.initialSetup.DRCRobotInitialSetup;
import us.ihmc.utilities.humanoidRobot.partNames.ArmJointName;
import us.ihmc.utilities.humanoidRobot.partNames.LegJointName;
import us.ihmc.utilities.humanoidRobot.partNames.SpineJointName;
import us.ihmc.utilities.io.printing.PrintTools;
import us.ihmc.utilities.math.geometry.RigidBodyTransform;
import us.ihmc.utilities.robotSide.RobotSide;
import us.ihmc.wholeBodyController.DRCRobotJointMap;

public class AtlasDrivingInitialSetup implements DRCRobotInitialSetup<SDFRobot>
{
   private static final String INITIAL_CONDITIONS_FILE = "initialDrivingSetup";
   private final AtlasJointMap atlasJointMap;
   
   private static final double PELVIS_TO_GROUND = 1.1;
   private static final double X_OFFSET = 0.1;
   private static final double Y_OFFSET = -0.6;
   
   private final RigidBodyTransform rootToWorld = new RigidBodyTransform();
   private final Vector3d positionInWorld = new Vector3d();
   private final Quat4d rotation = new Quat4d();
   private boolean robotInitialized = false;

   public AtlasDrivingInitialSetup(AtlasJointMap atlasJointMap)
   {
      this.atlasJointMap = atlasJointMap;
   }

   @Override
   public void initializeRobot(SDFRobot robot, DRCRobotJointMap jointMap)
   {
      if (robotInitialized)
      {
         return;
      }
      
      File file = new File(INITIAL_CONDITIONS_FILE);
      PrintTools.info("Loading initial joint configuration for driving simulation from " + file.getAbsolutePath());
      
      if (file.exists() && file.isFile())
      {
         try
         {
            Properties properties = new Properties();
            FileInputStream stream = new FileInputStream(file);
            properties.load(stream);
            
            for (RobotSide robotSide : RobotSide.values())
            {
               for (LegJointName jointName : LegJointName.values())
               {
                  String key = atlasJointMap.getLegJointName(robotSide, jointName);
                  if (key == null)
                  {
                     continue;
                  }
                  if (properties.containsKey(key))
                  {
                     String jointAngle = properties.getProperty(key);
                     robot.getOneDegreeOfFreedomJoint(jointMap.getLegJointName(robotSide, jointName)).setQ(Double.parseDouble(jointAngle) / 100.0);
                  }
                  else
                  {
                     PrintTools.info("Did not find initial angle for " + key);
                  }
               }
               
               for (ArmJointName jointName : ArmJointName.values())
               {
                  String key = atlasJointMap.getArmJointName(robotSide, jointName);
                  if (key == null)
                  {
                     continue;
                  }
                  if (properties.containsKey(key))
                  {
                     String jointAngle = properties.getProperty(key);
                     robot.getOneDegreeOfFreedomJoint(jointMap.getArmJointName(robotSide, jointName)).setQ(Double.parseDouble(jointAngle) / 100.0);
                  }
                  else
                  {
                     PrintTools.info("Did not find initial angle for " + key);
                  }
               }
            }
            
            for (SpineJointName jointName : SpineJointName.values())
            {
               String key = atlasJointMap.getSpineJointName(jointName);
               if (key == null)
               {
                  continue;
               }
               if (properties.containsKey(key))
               {
                  String jointAngle = properties.getProperty(key);
                  robot.getOneDegreeOfFreedomJoint(jointMap.getSpineJointName(jointName)).setQ(Double.parseDouble(jointAngle) / 100.0);
               }
               else
               {
                  PrintTools.info("Did not find initial angle for " + key);
               }
            }
            
            stream.close();
         }
         catch (IOException e)
         {
            throw new RuntimeException("Atlas joint parameter file " + file.getAbsolutePath() + " cannot be loaded. ", e);
         }
         catch (NumberFormatException e)
         {
            throw new RuntimeException("Make sure all fields ar doubles in " + file.getAbsolutePath(), e);
         }
      }
      else
      {
         System.out.println("File not found or invalid.");
      }
      
      robot.getOneDoFJoints()[1].getName();
      
      robot.update();
      robot.getRootJointToWorldTransform(rootToWorld);
      rootToWorld.get(rotation, positionInWorld);
      
      positionInWorld.set(X_OFFSET, Y_OFFSET, PELVIS_TO_GROUND);
      
      robot.setPositionInWorld(positionInWorld);
      robot.update();
      robotInitialized = true;
   }

   @Override
   public void getOffset(Vector3d offsetToPack)
   {
      offsetToPack.set(X_OFFSET, Y_OFFSET, PELVIS_TO_GROUND);
   }

   @Override
   public void setOffset(Vector3d offset)
   {
   }

   @Override
   public void setInitialYaw(double yaw)
   {
   }

   @Override
   public void setInitialGroundHeight(double groundHeight)
   {
   }

   @Override
   public double getInitialYaw()
   {
      return 0.0;
   }

   @Override
   public double getInitialGroundHeight()
   {
      return 0.0;
   }
}