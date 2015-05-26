package us.ihmc.simulationconstructionset.physics.collision.gdx;

import us.ihmc.simulationconstructionset.physics.ScsCollisionDetector;
import us.ihmc.simulationconstructionset.physics.collision.SCSCollisionDetectorTest;
import us.ihmc.yoUtilities.dataStructure.registry.YoVariableRegistry;

/**
 * @author Peter Abeles
 */
public class GdxCollisionDetectorTest extends SCSCollisionDetectorTest
{
   @Override
   public ScsCollisionDetector createCollisionInterface()
   {
      return new GdxCollisionDetector(new YoVariableRegistry("Dummy"), 1000);
   }
}