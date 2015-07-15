package object_manipulation_msgs;

public interface GraspPlanningRequest extends org.ros.internal.message.Message {
  static final java.lang.String _TYPE = "object_manipulation_msgs/GraspPlanningRequest";
  static final java.lang.String _DEFINITION = "# Requests that grasp planning be performed on the object to be grasped\n# returns a list of grasps to be tested and executed\n\n# the arm being used\nstring arm_name\n\n# the object to be grasped\nGraspableObject target\n\n# the name that the target object has in the collision environment\n# can be left empty if no name is available\nstring collision_object_name\n\n# the name that the support surface (e.g. table) has in the collision map\n# can be left empty if no name is available\nstring collision_support_surface_name\n\n# an optional list of grasps to be evaluated by the planner\nGrasp[] grasps_to_evaluate\n\n# an optional list of obstacles that we have semantic information about\n# and that can be moved in the course of grasping\nGraspableObject[] movable_obstacles\n\n";
  java.lang.String getArmName();
  void setArmName(java.lang.String value);
  object_manipulation_msgs.GraspableObject getTarget();
  void setTarget(object_manipulation_msgs.GraspableObject value);
  java.lang.String getCollisionObjectName();
  void setCollisionObjectName(java.lang.String value);
  java.lang.String getCollisionSupportSurfaceName();
  void setCollisionSupportSurfaceName(java.lang.String value);
  java.util.List<object_manipulation_msgs.Grasp> getGraspsToEvaluate();
  void setGraspsToEvaluate(java.util.List<object_manipulation_msgs.Grasp> value);
  java.util.List<object_manipulation_msgs.GraspableObject> getMovableObstacles();
  void setMovableObstacles(java.util.List<object_manipulation_msgs.GraspableObject> value);
}