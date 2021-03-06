package us.ihmc.commonWalkingControlModules.momentumBasedController.optimization;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.vecmath.Vector3d;

import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.LinearSolverFactory;
import org.ejml.interfaces.linsol.LinearSolver;
import org.ejml.ops.CommonOps;
import org.junit.Test;

import us.ihmc.commonWalkingControlModules.controllerCore.command.inverseDynamics.PointAccelerationCommand;
import us.ihmc.commonWalkingControlModules.momentumBasedController.GeometricJacobianHolder;
import us.ihmc.robotics.dataStructures.registry.YoVariableRegistry;
import us.ihmc.robotics.geometry.FramePoint;
import us.ihmc.robotics.geometry.FrameVector;
import us.ihmc.robotics.geometry.RigidBodyTransform;
import us.ihmc.robotics.random.RandomTools;
import us.ihmc.robotics.referenceFrames.CenterOfMassReferenceFrame;
import us.ihmc.robotics.referenceFrames.ReferenceFrame;
import us.ihmc.robotics.screwTheory.InverseDynamicsJoint;
import us.ihmc.robotics.screwTheory.RevoluteJoint;
import us.ihmc.robotics.screwTheory.RigidBody;
import us.ihmc.robotics.screwTheory.ScrewTestTools;
import us.ihmc.robotics.screwTheory.ScrewTools;
import us.ihmc.robotics.screwTheory.SpatialAccelerationCalculator;
import us.ihmc.robotics.screwTheory.TwistCalculator;
import us.ihmc.tools.testing.TestPlanAnnotations.DeployableTestMethod;

public class MotionQPInputCalculatorTest
{
   private static final ReferenceFrame worldFrame = ReferenceFrame.getWorldFrame();

   @DeployableTestMethod(estimatedDuration = 5.9)
   @Test(timeout = 30000)
   public void testPointAccelerationCommandsWithChainRobot() throws Exception
   {
      Random random = new Random(5641654L);

      for (int iteration = 0; iteration < 100; iteration++)
      {
         ArrayList<RevoluteJoint> joints = new ArrayList<>();
         ReferenceFrame elevatorFrame = ReferenceFrame.constructBodyFrameWithUnchangingTransformToParent("elevator", worldFrame, new RigidBodyTransform());
         RigidBody elevator = new RigidBody("elevator", elevatorFrame);
         int numberOfJoints = 10;
         Vector3d[] jointAxes = new Vector3d[numberOfJoints];
         for (int i = 0; i < numberOfJoints; i++)
            jointAxes[i] = RandomTools.generateRandomVector(random, 1.0);
         
         ScrewTestTools.createRandomChainRobot("blop", joints, elevator, jointAxes, random);
         ScrewTestTools.setRandomPositions(joints, random);
         ScrewTestTools.setRandomVelocities(joints, random);
         joints.get(0).getPredecessor().updateFramesRecursively();
         
         RigidBody endEffector = joints.get(joints.size() - 1).getSuccessor();
         FramePoint bodyFixedPointToControl = FramePoint.generateRandomFramePoint(random, endEffector.getBodyFixedFrame(), 1.0, 1.0, 1.0);
         FrameVector desiredLinearAcceleration = FrameVector.generateRandomFrameVector(random, elevatorFrame);
         
         
         YoVariableRegistry registry = new YoVariableRegistry("Dummy");
         ReferenceFrame centerOfMassFrame = new CenterOfMassReferenceFrame("centerOfMassFrame", worldFrame, elevator);
         GeometricJacobianHolder geometricJacobianHolder = new GeometricJacobianHolder();
         TwistCalculator twistCalculator = new TwistCalculator(worldFrame, elevator);
         twistCalculator.compute();
         InverseDynamicsJoint[] jointsToOptimizeFor = new InverseDynamicsJoint[numberOfJoints];
         joints.toArray(jointsToOptimizeFor);
         JointIndexHandler jointIndexHandler = new JointIndexHandler(jointsToOptimizeFor);
         
         MotionQPInputCalculator motionQPInputCalculator = new MotionQPInputCalculator(centerOfMassFrame, geometricJacobianHolder, twistCalculator, jointIndexHandler, 0.0, registry);
         
         PointAccelerationCommand pointAccelerationCommand = new PointAccelerationCommand();
         pointAccelerationCommand.set(elevator, endEffector);
         pointAccelerationCommand.setBodyFixedPointToControl(bodyFixedPointToControl);
         pointAccelerationCommand.setLinearAcceleration(desiredLinearAcceleration);
         
         MotionQPInput motionQPInput = new MotionQPInput(numberOfJoints);
         motionQPInputCalculator.convertPointAccelerationCommand(pointAccelerationCommand, motionQPInput);
         
         LinearSolver<DenseMatrix64F> pseudoInverseSolver = LinearSolverFactory.pseudoInverse(true);
         DenseMatrix64F jInverse = new DenseMatrix64F(numberOfJoints, 6);
         pseudoInverseSolver.setA(motionQPInput.taskJacobian);
         pseudoInverseSolver.invert(jInverse);
         
         DenseMatrix64F jointAccelerations = new DenseMatrix64F(numberOfJoints, 1);
         CommonOps.mult(jInverse, motionQPInput.taskObjective, jointAccelerations);
         
         ScrewTools.setDesiredAccelerations(jointsToOptimizeFor, jointAccelerations);
         
         SpatialAccelerationCalculator spatialAccelerationCalculator = new SpatialAccelerationCalculator(elevator, twistCalculator, 0.0, true);
         spatialAccelerationCalculator.compute();
         FrameVector actualLinearAcceleration = new FrameVector();
         spatialAccelerationCalculator.getLinearAccelerationOfBodyFixedPoint(actualLinearAcceleration, elevator, endEffector, bodyFixedPointToControl);
         
         assertTrue(actualLinearAcceleration.epsilonEquals(desiredLinearAcceleration, 1.0-7));
      }
   }

   @DeployableTestMethod(estimatedDuration = 5.9)
   @Test(timeout = 30000)
   public void testPointAccelerationCommandsWithFloatingChainRobot() throws Exception
   {
      Random random = new Random(5641654L);

      for (int iteration = 0; iteration < 100; iteration++)
      {
         int numberOfJoints = 10;
         Vector3d[] jointAxes = new Vector3d[numberOfJoints];
         for (int i = 0; i < numberOfJoints; i++)
            jointAxes[i] = RandomTools.generateRandomVector(random, 1.0);
         
         ScrewTestTools.RandomFloatingChain randomFloatingChain = new ScrewTestTools.RandomFloatingChain(random, jointAxes);
         List<RevoluteJoint> joints = randomFloatingChain.getRevoluteJoints();
         RigidBody elevator = randomFloatingChain.getElevator();

         ScrewTestTools.setRandomPositions(joints, random);
         ScrewTestTools.setRandomVelocities(joints, random);
         joints.get(0).getPredecessor().updateFramesRecursively();
         
         RigidBody endEffector = joints.get(joints.size() - 1).getSuccessor();
         FramePoint bodyFixedPointToControl = FramePoint.generateRandomFramePoint(random, endEffector.getBodyFixedFrame(), 1.0, 1.0, 1.0);
         FrameVector desiredLinearAcceleration = FrameVector.generateRandomFrameVector(random, elevator.getBodyFixedFrame());
         
         
         YoVariableRegistry registry = new YoVariableRegistry("Dummy");
         ReferenceFrame centerOfMassFrame = new CenterOfMassReferenceFrame("centerOfMassFrame", worldFrame, elevator);
         GeometricJacobianHolder geometricJacobianHolder = new GeometricJacobianHolder();
         TwistCalculator twistCalculator = new TwistCalculator(worldFrame, elevator);
         twistCalculator.compute();
         InverseDynamicsJoint[] jointsToOptimizeFor = ScrewTools.computeSupportAndSubtreeJoints(elevator);
         JointIndexHandler jointIndexHandler = new JointIndexHandler(jointsToOptimizeFor);
         
         MotionQPInputCalculator motionQPInputCalculator = new MotionQPInputCalculator(centerOfMassFrame, geometricJacobianHolder, twistCalculator, jointIndexHandler, 0.0, registry);
         
         PointAccelerationCommand pointAccelerationCommand = new PointAccelerationCommand();
         pointAccelerationCommand.set(elevator, endEffector);
         pointAccelerationCommand.setBodyFixedPointToControl(bodyFixedPointToControl);
         pointAccelerationCommand.setLinearAcceleration(desiredLinearAcceleration);
         
         int numberOfDoFs = ScrewTools.computeDegreesOfFreedom(jointsToOptimizeFor);
         MotionQPInput motionQPInput = new MotionQPInput(numberOfDoFs);
         motionQPInputCalculator.convertPointAccelerationCommand(pointAccelerationCommand, motionQPInput);
         
         LinearSolver<DenseMatrix64F> pseudoInverseSolver = LinearSolverFactory.pseudoInverse(true);
         DenseMatrix64F jInverse = new DenseMatrix64F(numberOfDoFs, 6);
         pseudoInverseSolver.setA(motionQPInput.taskJacobian);
         pseudoInverseSolver.invert(jInverse);
         
         DenseMatrix64F jointAccelerations = new DenseMatrix64F(numberOfDoFs, 1);
         CommonOps.mult(jInverse, motionQPInput.taskObjective, jointAccelerations);
         
         ScrewTools.setDesiredAccelerations(jointsToOptimizeFor, jointAccelerations);
         
         SpatialAccelerationCalculator spatialAccelerationCalculator = new SpatialAccelerationCalculator(elevator, twistCalculator, 0.0, true);
         spatialAccelerationCalculator.compute();
         FrameVector actualLinearAcceleration = new FrameVector();
         spatialAccelerationCalculator.getLinearAccelerationOfBodyFixedPoint(actualLinearAcceleration, elevator, endEffector, bodyFixedPointToControl);
         
         assertTrue(actualLinearAcceleration.epsilonEquals(desiredLinearAcceleration, 1.0-7));
      }
   }
}
