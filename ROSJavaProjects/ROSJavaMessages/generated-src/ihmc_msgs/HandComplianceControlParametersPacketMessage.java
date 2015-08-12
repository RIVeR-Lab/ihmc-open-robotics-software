package ihmc_msgs;

public interface HandComplianceControlParametersPacketMessage extends org.ros.internal.message.Message {
  static final java.lang.String _TYPE = "ihmc_msgs/HandComplianceControlParametersPacketMessage";
  static final java.lang.String _DEFINITION = "## HandComplianceControlParametersPacketMessage\n# This message setup the hand controller to activate a compliance module if at least one of the fields is set and deactivate if the message is empty.\n# To compliance module works only when the last hand command sent with the HandPosePacket is a hand pose (not joint angles).\n# Once activated, an integrator is used on the error in force/torque measured to keep adjusting the desired hand pose until the desired\n# force/torque are achieved or until the maximum correction is reached (set to 5cm for translation and 0.2rad for the orientation).\n# As it uses the measurements from wrist force sensors, a calibration of these is preferred prior to activation of compliance.\n\n# Options for robotSide\nuint8 LEFT=0 # refers to the LEFT side of a robot\nuint8 RIGHT=1 # refers to the RIGHT side of a robot\nuint8 robot_side\n\n# enableLinearCompliance allows to activate/deactivate the compliance in translation for each individual axes (X, Y, and Z).\n# The axes are in the hand control frame attached to the hand:\n#  - X refers to the axis perpendicular to the hand palm (e.g. forward/backward),\n#  - Y refers to the grasping axis (e.g. left/right),\n#  - Z refers to the axis orthogonal to the two other axes (e.g. up/down).\n# If the field is null, the linear compliance will be deactivated.\nbool[] enable_linear_compliance\n\n# enableAngularCompliance allows to activate/deactivate the compliance in orientation for each individual axes (X, Y, and Z).\n# The axes are in the hand control frame attached to the hand:\n#  - X refers to the axis perpendicular to the hand palm,\n#  - Y refers to the grasping axis,\n#  - Z refers to the axis orthogonal to the two other axes.\n# If the field is null, the angular compliance will be deactivated.\nbool[] enable_angular_compliance\n\n# desiredForce allows to set the desired force to be achieved on the hand for each individual axes (X, Y, and Z).\n# The axes are in the hand control frame attached to the hand:\n#  - X refers to the axis perpendicular to the hand palm (e.g. forward/backward),\n#  - Y refers to the grasping axis (e.g. left/right),\n#  - Z refers to the axis orthogonal to the two other axes (e.g. up/down).\n# If the field is null, the desired force will be set to zero.\ngeometry_msgs/Vector3 desired_force\n\n# desiredTorque allows to set the desired torque to be achieved on the hand for each individual axes (X, Y, and Z).\n# The axes are in the hand control frame attached to the hand:\n#  - X refers to the axis perpendicular to the hand palm,\n#  - Y refers to the grasping axis,\n#  - Z refers to the axis orthogonal to the two other axes.\n# If the field is null, the desired torque will be set to zero.\ngeometry_msgs/Vector3 desired_torque\n\n# wrenchDeadzones set the deadzones that are used on the force and torque measurements, respectively.\n# For instance, if wrenchDeadzones = {5.0, 0.5}, the controller will perceive only forces that are outside the range [-5N, 5N],\n# and torques that are outside the range [-0.5N.m, 0.5N.m].\n# As results, the compliance control will start adjusting the desired hand pose only for measured forces/torques greater\n# than the specified deadzones.\n# If the field is null, the deadzone won\'t be changed.\n# We have found that wrenchDeadzones = {10.0, 0.5} does not affect much the position control accuracy of the hand but still gives good compliance.\nfloat32[] wrench_deadzones\n\n\n";
  static final byte LEFT = 0;
  static final byte RIGHT = 1;
  byte getRobotSide();
  void setRobotSide(byte value);
  boolean[] getEnableLinearCompliance();
  void setEnableLinearCompliance(boolean[] value);
  boolean[] getEnableAngularCompliance();
  void setEnableAngularCompliance(boolean[] value);
  geometry_msgs.Vector3 getDesiredForce();
  void setDesiredForce(geometry_msgs.Vector3 value);
  geometry_msgs.Vector3 getDesiredTorque();
  void setDesiredTorque(geometry_msgs.Vector3 value);
  float[] getWrenchDeadzones();
  void setWrenchDeadzones(float[] value);
}