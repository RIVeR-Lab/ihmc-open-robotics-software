package us.ihmc.humanoidBehaviors.behaviors;

import us.ihmc.robotics.dataStructures.variable.BooleanYoVariable;
import us.ihmc.robotics.dataStructures.variable.DoubleYoVariable;
import us.ihmc.robotics.dataStructures.variable.IntegerYoVariable;

public abstract class KickBallBehaviorCoactiveElement extends BehaviorCoactiveElement
{
   //UI SIDE YOVARS
   public final IntegerYoVariable userInterfaceSideCount = new IntegerYoVariable("userInterfaceSideCount", userInterfaceWritableRegistry);
   public final BooleanYoVariable abortClicked = new BooleanYoVariable("abortClicked", userInterfaceWritableRegistry);
   public final BooleanYoVariable validClicked = new BooleanYoVariable("validClicked", userInterfaceWritableRegistry);

   //BEHAVIOR SIDE YOVARS
   public final IntegerYoVariable machineSideCount = new IntegerYoVariable("machineSideCount", machineWritableRegistry);
   public final IntegerYoVariable abortCount = new IntegerYoVariable("abortCount", machineWritableRegistry);
   public final BooleanYoVariable abortAcknowledged = new BooleanYoVariable("abortAcknowledged", machineWritableRegistry);
   public final BooleanYoVariable searchingForBall = new BooleanYoVariable("searchingForBall", machineWritableRegistry);
   public final BooleanYoVariable foundBall = new BooleanYoVariable("foundBall", machineWritableRegistry);

   public final IntegerYoVariable numBlobsDetected = new IntegerYoVariable("numBlobsDetected", machineWritableRegistry);
   public final DoubleYoVariable blobX = new DoubleYoVariable("blobX", machineWritableRegistry);
   public final DoubleYoVariable blobY = new DoubleYoVariable("blobY", machineWritableRegistry);

   public final DoubleYoVariable ballX = new DoubleYoVariable("ballX", machineWritableRegistry);
   public final DoubleYoVariable ballY = new DoubleYoVariable("ballY", machineWritableRegistry);
   public final DoubleYoVariable ballZ = new DoubleYoVariable("ballZ", machineWritableRegistry);

   public final DoubleYoVariable ballRadius = new DoubleYoVariable("ballRadius", machineWritableRegistry);
   public final BooleanYoVariable validAcknowledged = new BooleanYoVariable("validAcknowledged", machineWritableRegistry);
   public final BooleanYoVariable waitingForValidation = new BooleanYoVariable("waitingForValidation", machineWritableRegistry);
}
