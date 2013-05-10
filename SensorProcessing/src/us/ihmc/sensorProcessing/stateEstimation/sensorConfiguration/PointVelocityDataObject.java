package us.ihmc.sensorProcessing.stateEstimation.sensorConfiguration;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import us.ihmc.utilities.math.geometry.FramePoint;
import us.ihmc.utilities.math.geometry.FrameVector;
import us.ihmc.utilities.screwTheory.RigidBody;

public class PointVelocityDataObject
{
   protected String rigidBodyName;
   protected String bodyFixedReferenceFrameName;
   protected final Point3d measurementPointInBodyFrame = new Point3d();
   protected final Vector3d velocityOfMeasurementPointInWorldFrame = new Vector3d();

   public void set(RigidBody rigidBody, FramePoint measurementPointInBodyFrame, FrameVector velocityOfMeasurementPointInWorldFrame)
   {
      this.rigidBodyName = rigidBody.getName();
      this.bodyFixedReferenceFrameName = measurementPointInBodyFrame.getReferenceFrame().getName();
      measurementPointInBodyFrame.getPoint(this.measurementPointInBodyFrame);
      velocityOfMeasurementPointInWorldFrame.getVector(this.velocityOfMeasurementPointInWorldFrame);
   }

   public String getRigidBodyName()
   {
      return rigidBodyName;
   }

   public Vector3d getVelocityOfMeasurementPointInWorldFrame()
   {
      return velocityOfMeasurementPointInWorldFrame;
   }

   public Point3d getMeasurementPointInBodyFrame()
   {
      return measurementPointInBodyFrame;
   }

   public void set(PointVelocityDataObject other)
   {
      this.rigidBodyName = other.rigidBodyName;
      this.bodyFixedReferenceFrameName = other.bodyFixedReferenceFrameName;
      this.measurementPointInBodyFrame.set(other.measurementPointInBodyFrame);
      this.velocityOfMeasurementPointInWorldFrame.set(other.velocityOfMeasurementPointInWorldFrame);
   }

   public boolean epsilonEquals(PointVelocityDataObject other, double epsilon)
   {
      if (this.bodyFixedReferenceFrameName != other.bodyFixedReferenceFrameName)
         return false;

      boolean rigidBodyEqual = other.rigidBodyName == this.rigidBodyName;
      boolean bodyPointsEqual = getMeasurementPointInBodyFrame().epsilonEquals(other.getMeasurementPointInBodyFrame(), epsilon);
      boolean worldVelocitiesEqual = getVelocityOfMeasurementPointInWorldFrame().epsilonEquals(other.getVelocityOfMeasurementPointInWorldFrame(), epsilon);
      return rigidBodyEqual && bodyPointsEqual && worldVelocitiesEqual;
   }

   public String getBodyFixedReferenceFrameName()
   {
      return bodyFixedReferenceFrameName;
   }

}
