package us.ihmc.commonWalkingControlModules.desiredHeadingAndVelocity;

import us.ihmc.utilities.math.geometry.FrameVector2d;

public interface DesiredVelocityControlModule
{
   public abstract void getDesiredVelocity(FrameVector2d desiredVelocityToPack);

   public abstract void updateDesiredVelocity();
}