package us.ihmc.humanoidRobotics.bipedSupportPolygons;

import java.util.List;

import us.ihmc.robotics.geometry.FramePoint;
import us.ihmc.robotics.referenceFrames.ReferenceFrame;
import us.ihmc.robotics.screwTheory.RigidBody;

public interface ContactableBody
{
   public abstract String getName();

   public abstract RigidBody getRigidBody();

   public abstract ReferenceFrame getFrameAfterParentJoint();

   public abstract int getTotalNumberOfContactPoints();

   public abstract List<FramePoint> getContactPointsCopy();
}
