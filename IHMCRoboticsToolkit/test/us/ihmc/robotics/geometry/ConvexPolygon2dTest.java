package us.ihmc.robotics.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import org.junit.Test;

import us.ihmc.robotics.referenceFrames.ReferenceFrame;
import us.ihmc.tools.testing.TestPlanAnnotations.DeployableTestMethod;

public class ConvexPolygon2dTest
{
   private static final boolean VERBOSE = false;

   private Random random = new Random(1176L);
   private static final boolean PLOT_RESULTS = false;
   private static final boolean WAIT_FOR_BUTTON_PUSH = false;
   private final double epsilon = 1e-10;

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testConstructors()
   {
      ConvexPolygon2d defaultConstructor = new ConvexPolygon2d();
      assertEquals("Number of vertices should be zero", 0.0, defaultConstructor.getNumberOfVertices(), epsilon);
      assertTrue(defaultConstructor.isUpToDate());

      int numberOfVertices = 4;
      ArrayList<Point2d> verticesList = new ArrayList<Point2d>();
      verticesList.add(new Point2d(0.0, 0.0));
      verticesList.add(new Point2d(0.0, 1.0));
      verticesList.add(new Point2d(1.0, 0.0));
      verticesList.add(new Point2d(1.0, 1.0));

      ConvexPolygon2d listInt = new ConvexPolygon2d(verticesList, numberOfVertices);
      assertEquals("Number of vertices should be 4", 4.0, listInt.getNumberOfVertices(), epsilon);

      ConvexPolygon2d list = new ConvexPolygon2d(verticesList);
      assertEquals("Number of vertices should be 4", 4.0, list.getNumberOfVertices(), epsilon);

      double[][] verticesArray = {
            {0.0, 0.0},
            {0.0, 1.0},
            {1.0, 0.0},
            {1.0, 1.0}
      };

      ConvexPolygon2d doubleInt = new ConvexPolygon2d(verticesArray, numberOfVertices);
      assertEquals("Number of vertices should be four", 4.0, doubleInt.getNumberOfVertices(), epsilon);
      assertTrue(doubleInt.isUpToDate());

      ConvexPolygon2d doubles = new ConvexPolygon2d(verticesArray);
      assertEquals("Number of vertices should be four", 4.0, doubles.getNumberOfVertices(), epsilon);
      assertTrue(doubles.isUpToDate());

      ConvexPolygon2d polygon = new ConvexPolygon2d(doubles);
      assertEquals("Number of vertices should be four", 4.0, polygon.getNumberOfVertices(), epsilon);
      assertTrue(polygon.isUpToDate());

      ConvexPolygon2d polygonPolygon = new ConvexPolygon2d(doubleInt, doubles);
      assertEquals("Number of vertices should be four", 4.0, polygonPolygon.getNumberOfVertices(), epsilon);
      assertTrue(polygonPolygon.isUpToDate());
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testClear()
   {
      ArrayList<Point2d> verticesList = new ArrayList<Point2d>();
      verticesList.add(new Point2d(0.0, 0.0));
      verticesList.add(new Point2d(0.0, 1.0));
      verticesList.add(new Point2d(1.0, 0.0));
      verticesList.add(new Point2d(1.0, 1.0));

      ConvexPolygon2d list = new ConvexPolygon2d(verticesList);
      assertEquals("Number of vertices should be 4", 4.0, list.getNumberOfVertices(), epsilon);
      assertTrue(list.isUpToDate());
      list.clear();
      assertEquals("Number of vertices should be 0", 0.0, list.getNumberOfVertices(), epsilon);
      assertFalse(list.isUpToDate());
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testSetAndUpdates()
   {
      ConvexPolygon2d doubleInt = new ConvexPolygon2d();
      int numberOfVertices = 4;
      double[][] verticesArray = {
            {0.0, 0.0},
            {0.0, 1.0},
            {1.0, 0.0},
            {1.0, 1.0}
      };
      doubleInt.setAndUpdate(verticesArray, numberOfVertices);
      assertEquals("Number of vertices should be 4", 4.0, doubleInt.getNumberOfVertices(), epsilon);
      assertTrue(doubleInt.isUpToDate());
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testGetCentroid()
   {
      double[][] verticesArray = {
            {0.0, 0.0},
            {0.0, 1.0},
            {1.0, 0.0},
            {1.0, 1.0}
      };
      ConvexPolygon2d doubles = new ConvexPolygon2d(verticesArray);
      Point2d centroid = new Point2d();

      doubles.getCentroid(centroid);
      assertEquals("Centroids should be equal", centroid, doubles.getCentroid());
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testGetBoundingBox()
   {
      double[][] verticesArray = {
            {0.0, 0.0},
            {0.0, 1.0},
            {1.0, 0.0},
            {1.0, 1.0}
      };
      ConvexPolygon2d doubles = new ConvexPolygon2d(verticesArray);
      BoundingBox2d box = doubles.getBoundingBox();

      assertEquals("Bounding boxes should be equal", box.getMinPoint().getX(), 0.0, epsilon);
      assertEquals("Bounding boxes should be equal", box.getMinPoint().getX(), 0.0, epsilon);
      assertEquals("Bounding boxes should be equal", box.getMaxPoint().getY(), 1.0, epsilon);
      assertEquals("Bounding boxes should be equal", box.getMaxPoint().getY(), 1.0, epsilon);
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testGetNextVertexCCWGetPreviousVertexCCW()
   {
      double[][] verticesArray = {
            {0.0, 0.0},
            {0.0, 1.0},
            {1.0, 0.0},
            {1.0, 1.0}
      };
      ConvexPolygon2d doubles = new ConvexPolygon2d(verticesArray);

      Point2d oneNext = doubles.getNextVertexCCW(0); //(1.0, 0.0)
      Point2d twoNext = doubles.getNextVertexCCW(1); //(1.0, 1.0)
      Point2d threeNext = doubles.getNextVertexCCW(2); //(0.0, 1.0)
      Point2d fourNext = doubles.getNextVertexCCW(3); //(0.0, 0.0)

      Point2d onePrev = doubles.getPreviousVertexCCW(0); //(0.0, 1.0)
      Point2d twoPrev = doubles.getPreviousVertexCCW(1); //(0.0, 0.0)
      Point2d threePrev = doubles.getPreviousVertexCCW(2); //(1.0, 0.0)
      Point2d fourPrev = doubles.getPreviousVertexCCW(3); //(1.0, 1.0)

      assertEquals("Points should be equal", oneNext, threePrev);
      assertEquals("Points should be equal", twoNext, fourPrev);
      assertEquals("Points should be equal", threeNext, onePrev);
      assertEquals("Points should be equal", fourNext, twoPrev);
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testScale()
   {
      double scaleFactor = 2.0;
      double[][] verticesArray = {
            {-1.0, 1.0},
            {1.0, 1.0},
            {1.0, -1.0},
            {-1.0, -1.0}
      };

      ConvexPolygon2d polygon = new ConvexPolygon2d(verticesArray);
      polygon.scale(scaleFactor);

      Point2d oneNext = polygon.getNextVertexCCW(0); //(2.0, -2.0)
      Point2d twoNext = polygon.getNextVertexCCW(1); //(2.0, 2.0)
      Point2d threeNext = polygon.getNextVertexCCW(2); //(-2.0, 2.0)
      Point2d fourNext = polygon.getNextVertexCCW(3); //(-2.0, -2.0)

      Point2d one = new Point2d(2.0, -2.0);
      Point2d two = new Point2d(2.0, 2.0);
      Point2d three = new Point2d(-2.0, 2.0);
      Point2d four = new Point2d(-2.0, -2.0);

      assertEquals("These should be equal", oneNext, one);
      assertEquals("These should be equal", twoNext, two);
      assertEquals("These should be equal", threeNext, three);
      assertEquals("These should be equal", fourNext, four);
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testIsPointInside()
   {
      Random random = new Random(4564656L);
      double[][] verticesArray = {
            {-10.0, 10.0},
            {10.0, 10.0},
            {10.0, -10.0},
            {-10.0, -10.0}
      };
      ConvexPolygon2d doubles = new ConvexPolygon2d(verticesArray);
      
      for(int i = 0; i < 10; i++)
      {
         int x = random.nextInt(10);
         int y = random.nextInt(10);
         assertTrue(doubles.isPointInside(x, y));
      } 
   }
   
   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testIntersectionWith()
   {
      double[][] verticesArray1 = {
            {-10.0, 10.0},
            {10.0, 10.0},
            {10.0, -10.0},
            {-10.0, -10.0}
      };
      double[][] verticesArray2 = {
            {-5.0, 5.0},
            {5.0, 5.0},
            {5.0, -5.0},
            {-5.0, -5.0}
      };
      double[][] verticesArray3 = {
            {15.0, 20.0},
            {20.0, 20.0},
            {20.0, 15.0},
            {15.0, 15.0}
      };
      double[][] verticesArray4 = {
            {-5.0, -10.0},
            {-5.0, 10.0},
            {15.0, -10.0},
            {15.0, 10.0}
      };
      
      ConvexPolygon2d polygon1 = new ConvexPolygon2d(verticesArray1);
      ConvexPolygon2d polygon2 = new ConvexPolygon2d(verticesArray2);
      ConvexPolygon2d polygon3 = new ConvexPolygon2d(verticesArray3);
      ConvexPolygon2d polygon4 = new ConvexPolygon2d(verticesArray4);

      ConvexPolygon2d noIntersect = new ConvexPolygon2d();
      ConvexPolygon2d allIntersect = new ConvexPolygon2d();
      ConvexPolygon2d someIntersect = new ConvexPolygon2d();


      assertTrue(polygon1.intersectionWith(polygon2, allIntersect));
      assertFalse("Should be false", polygon1.intersectionWith(polygon3, noIntersect));
      assertTrue(polygon1.intersectionWith(polygon4, someIntersect));
//
//      System.out.println("No" + noIntersect);
//      System.out.println("Some" + someIntersect);
//      System.out.println("all" + allIntersect);
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testTriangleConstructor()
   {
      double[][] vertices = new double[][] { { 0.0, 0.0 }, { 2.0, 0.0 }, { 1.0, 0.1 } };

      ReferenceFrame zUpFrame = ReferenceFrame.constructARootFrame("someFrame", true, false, true);

      FrameConvexPolygon2d polygon = ConvexPolygon2dTestHelpers.constructPolygon(zUpFrame, vertices);
      assertEquals(3, polygon.getNumberOfVertices());

      ConvexPolygon2dTestHelpers.verifyPolygonContains(polygon, new FramePoint2d(zUpFrame, 0.0, 0.0), 1e-10);
      ConvexPolygon2dTestHelpers.verifyPolygonContains(polygon, new FramePoint2d(zUpFrame, 2.0, 0.0), 1e-10);
      ConvexPolygon2dTestHelpers.verifyPolygonContains(polygon, new FramePoint2d(zUpFrame, 1.0, 0.1), 1e-10);

      ConvexPolygon2dTestHelpers.verifyPointsAreClockwise(polygon);
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testIntersectionWhenFullyInside()
   {
      ArrayList<Point2d> listOfPoints = new ArrayList<Point2d>();
      listOfPoints.add(new Point2d(0.0, 0.0));
      listOfPoints.add(new Point2d(1.0, 0.0));
      listOfPoints.add(new Point2d(0.0, 1.0));
      listOfPoints.add(new Point2d(1.0, 1.0));

      ConvexPolygon2d convexPolygon2dA = new ConvexPolygon2d(listOfPoints);

      listOfPoints.clear();
      listOfPoints.add(new Point2d(-1.0, -1.0));
      listOfPoints.add(new Point2d(2.0, -1.0));
      listOfPoints.add(new Point2d(-1.0, 2.0));
      listOfPoints.add(new Point2d(2.0, 2.0));

      ConvexPolygon2d convexPolygon2dB = new ConvexPolygon2d(listOfPoints);

      ConvexPolygon2d intersection = new ConvexPolygon2d();
      ConvexPolygonTools.computeIntersectionOfPolygons(convexPolygon2dA, convexPolygon2dB, intersection);
      boolean epsilonEquals = intersection.epsilonEquals(convexPolygon2dA, 1e-7);
      assertTrue(epsilonEquals);

      ConvexPolygonTools.computeIntersectionOfPolygons(convexPolygon2dB, convexPolygon2dA, intersection);
      epsilonEquals = intersection.epsilonEquals(convexPolygon2dA, 1e-7);
      assertTrue(epsilonEquals);

      listOfPoints.clear();
      listOfPoints.add(new Point2d(0.1904001452623111, 0.07922536690619195));
      listOfPoints.add(new Point2d(0.1923482408479345, 0.5736513188711437));
      listOfPoints.add(new Point2d(0.24837080387208538, 0.5533707067242215));
      listOfPoints.add(new Point2d(0.2560381177005394, 0.550093244819894));
      listOfPoints.add(new Point2d(0.3021057612864858, 0.5276338625408057));
      listOfPoints.add(new Point2d(0.35302325196142154, 0.49669456810449586));
      listOfPoints.add(new Point2d(0.4006211967955147, 0.4608579046936889));
      listOfPoints.add(new Point2d(0.4444302495375464, 0.42047724478458476));
      listOfPoints.add(new Point2d(0.4840184248413931, 0.3759507675720234));
      listOfPoints.add(new Point2d(0.5189953579184864, 0.3277175326673503));
      listOfPoints.add(new Point2d(0.5490161537848919, 0.27625315068916595));
      listOfPoints.add(new Point2d(0.5737847881469639, 0.2220650934377122));
      listOfPoints.add(new Point2d(0.5930570263906623, 0.16568768989757945));
      listOfPoints.add(new Point2d(0.606642831891427, 0.10767685741135981));
      listOfPoints.add(new Point2d(0.1904001452623111, 0.07922536690619195));
      convexPolygon2dA = new ConvexPolygon2d(listOfPoints);

      listOfPoints.clear();
      listOfPoints.add(new Point2d(-0.26792484945022277, 0.5164452162023662));
      listOfPoints.add(new Point2d(-0.21938799685279367, 0.5422255592213991));
      listOfPoints.add(new Point2d(-0.1686958167513698, 0.5634565512568254));
      listOfPoints.add(new Point2d(-0.11627362387979798, 0.5799600612101443));
      listOfPoints.add(new Point2d(-0.06256124802966133, 0.591597622242303));
      listOfPoints.add(new Point2d(-0.008009343814616467, 0.5982715935305327));
      listOfPoints.add(new Point2d(0.04692439038709253, 0.5999259794889963));
      listOfPoints.add(new Point2d(0.10177905258832422, 0.5965468995798632));
      listOfPoints.add(new Point2d(0.1560944042274756, 0.5881627047730331));
      listOfPoints.add(new Point2d(0.20941473163667895, 0.5748437396773916));
      listOfPoints.add(new Point2d(0.26129266954548536, 0.5567017523393519));
      listOfPoints.add(new Point2d(0.3112929545402855, 0.5338889566605598));
      listOfPoints.add(new Point2d(0.3589960769873979, 0.5065967553012091));
      listOfPoints.add(new Point2d(0.40400180077966186, 0.4750541337839984));
      listOfPoints.add(new Point2d(0.4459325213753508, 0.43952573927242716));
      listOfPoints.add(new Point2d(0.48443643395497327, 0.4003096601427597));
      listOfPoints.add(new Point2d(0.5191904851146687, 0.3577349249793695));
      listOfPoints.add(new Point2d(0.5499030833310595, 0.31215874197725635));
      listOfPoints.add(new Point2d(0.5763165454563693, 0.26396350191354906));
      listOfPoints.add(new Point2d(0.5982092587173592, 0.2135535698334953));
      listOfPoints.add(new Point2d(0.6153975400785948, 0.16135189236915945));
      listOfPoints.add(new Point2d(0.6277371773697216, 0.10779644915591552));
      listOfPoints.add(new Point2d(0.6351246392464617, 0.05333657811986491));
      listOfPoints.add(new Point2d(0.6374979438335908, -0.00157079453245079));
      listOfPoints.add(new Point2d(0.634837178761848, -0.056464987991108585));
      listOfPoints.add(new Point2d(0.0, 0.06));
      convexPolygon2dB = new ConvexPolygon2d(listOfPoints);

      ConvexPolygonTools.computeIntersectionOfPolygons(convexPolygon2dB, convexPolygon2dA, intersection);
      epsilonEquals = intersection.epsilonEquals(convexPolygon2dA, 1e-14);
      assertTrue(epsilonEquals);

      ConvexPolygonTools.computeIntersectionOfPolygons(convexPolygon2dA, convexPolygon2dB, intersection);
      epsilonEquals = intersection.epsilonEquals(convexPolygon2dA, 1e-14);
      assertTrue(epsilonEquals);
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testIntersectionWhenFullyInsideWithRepeatedPoint()
   {
      ArrayList<Point2d> listOfPoints = new ArrayList<Point2d>();
      listOfPoints.add(new Point2d(0.19, 0.0));
      listOfPoints.add(new Point2d(0.192, 0.6));
      listOfPoints.add(new Point2d(0.25, 0.5));
      listOfPoints.add(new Point2d(0.19, 0.0));
      ConvexPolygon2d convexPolygon2dA = new ConvexPolygon2d(listOfPoints);

      listOfPoints.clear();
      listOfPoints.add(new Point2d(-1.0, -1.0));
      listOfPoints.add(new Point2d(2.0, -1.0));
      listOfPoints.add(new Point2d(-1.0, 2.0));
      listOfPoints.add(new Point2d(2.0, 2.0));
      ConvexPolygon2d convexPolygon2dB = new ConvexPolygon2d(listOfPoints);

      ConvexPolygon2d intersection = new ConvexPolygon2d();
      ConvexPolygonTools.computeIntersectionOfPolygons(convexPolygon2dA, convexPolygon2dB, intersection);
      boolean epsilonEquals = intersection.epsilonEquals(convexPolygon2dA, 1e-14);
      assertTrue(epsilonEquals);

      ConvexPolygonTools.computeIntersectionOfPolygons(convexPolygon2dB, convexPolygon2dA, intersection);
      epsilonEquals = intersection.epsilonEquals(convexPolygon2dA, 1e-14);
      assertTrue(epsilonEquals);
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testConstructorWithRepeatedPoints()
   {
      ArrayList<Point2d> listOfPoints = new ArrayList<Point2d>();
      listOfPoints.add(new Point2d(0.0, 0.0));
      listOfPoints.add(new Point2d(1.0, 1.0));
      listOfPoints.add(new Point2d(1.0, 1.0));
      listOfPoints.add(new Point2d(1.0, 1.0));
      listOfPoints.add(new Point2d(1.0, 0.0));
      listOfPoints.add(new Point2d(0.0, 0.0));
      ConvexPolygon2d convexPolygon2d = new ConvexPolygon2d(listOfPoints);

      // Above point list contains 3 unique points
      assertEquals(3, convexPolygon2d.getNumberOfVertices());
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testNANRay()
   {
      ArrayList<Point2d> listOfPoints = new ArrayList<Point2d>();
      listOfPoints.add(new Point2d(0.11429999999999998, 0.1397));
      listOfPoints.add(new Point2d(0.11429999999999998, 0.04444999999999999));
      listOfPoints.add(new Point2d(-0.047625, 0.04444999999999999));
      listOfPoints.add(new Point2d(-0.047625, 0.1397));

      ConvexPolygon2d convexPolygon2d = new ConvexPolygon2d(listOfPoints);

      Point2d pont2d = new Point2d(Double.NaN, Double.NaN);
      Vector2d vector2d = new Vector2d(Double.NaN, Double.NaN);
      Line2d line2d = new Line2d(pont2d, vector2d);

      convexPolygon2d.intersectionWithRay(line2d);
      System.out.println("done");
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testIsInside()
   {
      double[][] polygonPoints = new double[][] { { -0.05107802536335158, 0.04155594197133163 }, { -0.05052044462374434, 0.1431544119584275 },
            { 0.12219695435431863, 0.14220652470109518 }, { 0.12219695435431865, -0.041946248489056696 }, { 0.12163937361471142, -0.1435447184761526 },
            { -0.05107802536335154, -0.14259683121882027 } };

      Point2d testPoint = new Point2d(-0.04907805548171582, 2.6934439541712686E-4);

      ConvexPolygon2d polygon = new ConvexPolygon2d(polygonPoints);

      boolean isInside = polygon.isPointInside(testPoint);
      System.out.println("isInside = " + isInside);

      assertTrue(isInside);
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testDistancePoint2dConvexPolygon2d()
    {
       ArrayList<Point2d> points = new ArrayList<Point2d>();
       points.add(new Point2d());
       points.add(new Point2d());
       points.add(new Point2d());
       ConvexPolygon2d test = new ConvexPolygon2d(points);
       test.distance(new Point2d());
    }

   
   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testPerimeterSquare()
   {
      Random random = new Random(1001L);

      ReferenceFrame zUpFrame = ReferenceFrame.constructARootFrame("someFrame", true, false, true);
      double epsilon = 1E-12;

      int numberOfTests = 100;
      for (int i = 0; i < numberOfTests; i++)
      {
         double xMin = random.nextDouble();
         double xMax = xMin + random.nextDouble();
         double yMin = random.nextDouble();
         double yMax = yMin + random.nextDouble();

         ArrayList<FramePoint2d> squareList = new ArrayList<FramePoint2d>();
         squareList.add(new FramePoint2d(zUpFrame, xMin, yMin));
         squareList.add(new FramePoint2d(zUpFrame, xMax, yMax));
         squareList.add(new FramePoint2d(zUpFrame, xMin, yMax));
         squareList.add(new FramePoint2d(zUpFrame, xMax, yMin));
         FrameConvexPolygon2d square = new FrameConvexPolygon2d(squareList);

         double perimeter = square.perimeter();
         double expectedPerimeter = 2.0 * (xMax - xMin) + 2.0 * (yMax - yMin);
         assertEquals("Perimeter not calculated correctly for a square", expectedPerimeter, perimeter, epsilon);
      }
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testExtremePointsSquare()
   {
      ReferenceFrame zUpFrame = ReferenceFrame.constructARootFrame("someFrame", true, false, true);
      double xMin = -.1;
      double xMax = 0.1;
      double yMin = -0.1;
      double yMax = 0.1;

      ArrayList<FramePoint2d> squareList = new ArrayList<FramePoint2d>();
      squareList.add(new FramePoint2d(zUpFrame, xMin, yMin));
      squareList.add(new FramePoint2d(zUpFrame, xMax, yMax));
      squareList.add(new FramePoint2d(zUpFrame, xMin, yMax));
      squareList.add(new FramePoint2d(zUpFrame, xMax, yMin));

      FrameConvexPolygon2d square = new FrameConvexPolygon2d(squareList);

      // Compute the extreme points:
      FramePoint2d frontmostLeft = square.getMinXMaxYPointCopy();
      FramePoint2d frontmostRight = square.getMaxXMaxYPointCopy();
      FramePoint2d backmostRight = square.getMaxXMinYPointCopy();
      FramePoint2d backmostLeft = square.getMinXMinYPointCopy();

      for (int i = 0; i < square.getNumberOfVertices(); i++)
      {
         FramePoint2d point = square.getFrameVertexCopy(i);
         assertFalse("frontmostLeft wrong, frontMostLeft = " + frontmostLeft + ", point = " + point,
               ((point.getX() < frontmostLeft.getX()) || ((point.getX() == frontmostLeft.getX()) && (point.getY() > frontmostLeft.getY()))));
         assertFalse("frontmostRight wrong, frontmostRight = " + frontmostRight + ", point = " + point,
               ((point.getX() > frontmostRight.getX()) || ((point.getX() == frontmostRight.getX()) && (point.getY() > frontmostRight.getY()))));
         assertFalse("backmostRight wrong, backmostRight = " + backmostRight + ", point = " + point,
               ((point.getX() > backmostRight.getX()) || ((point.getX() == backmostRight.getX()) && (point.getY() < backmostRight.getY()))));
         assertFalse("backmostLeft wrong, backmostLeft = " + backmostLeft + ", point = " + point,
               ((point.getX() < backmostLeft.getX()) || ((point.getX() == backmostLeft.getX()) && (point.getY() < backmostLeft.getY()))));
      }

      if (PLOT_RESULTS)
      {
         // Plot:
         FrameGeometryTestFrame testFrame = new FrameGeometryTestFrame(2.0 * xMin, 2.0 * xMax, 2.0 * yMin, 2.0 * yMax);
         FrameGeometry2dPlotter plotter = testFrame.getFrameGeometry2dPlotter();
         plotter.setDrawPointsLarge();
         plotter.addPolygon(square);
         plotter.addFramePoint2d(frontmostLeft, Color.green);
         plotter.addFramePoint2d(frontmostRight, Color.orange);
         plotter.addFramePoint2d(backmostRight, Color.red);
         plotter.addFramePoint2d(backmostLeft, Color.black);

         waitForButtonOrPause(testFrame);
      }
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testInsideWithSimpleSquare()
   {
      double[][] vertices = new double[][] { { 0.0, 0.0 }, { 1.0, 0.0 }, { 0.0, 1.0 }, { 1.0, 1.0 } };
      ReferenceFrame zUpFrame = ReferenceFrame.constructARootFrame("someFrame", true, false, true);
      FrameConvexPolygon2d polygon = ConvexPolygon2dTestHelpers.constructPolygon(zUpFrame, vertices);

      double[][] pointsOutside = new double[][] { { 1.5, 0.5 }, { 1.1, 1.1 }, { 1.1, 0.5 }, { -0.1, -0.1 }, { -0.1, 0.5 } };
      double[][] pointsInside = new double[][] { { 0.5, 0.5 }, { 0.1, 0.7 }, { 0.3, 0.5 }, { 0.99, 0.99 }, { 0.01, 0.01 }, { 0.01, 0.99 }, { 0.99, 0.01 } };
      double[][] boundaryPoints = new double[][] { { 0.0, 0.5 }, { 0.5, 0.0 }, { 1.0, 0.5 }, { 0.5, 1.0 } };

      for (double[] pointOutside : pointsOutside)
      {
         FramePoint2d testPoint = new FramePoint2d(zUpFrame, pointOutside);
         if (polygon.isPointInside(testPoint))
            throw new RuntimeException();
      }

      for (double[] pointInside : pointsInside)
      {
         FramePoint2d testPoint = new FramePoint2d(zUpFrame, pointInside);
         if (!polygon.isPointInside(testPoint))
            throw new RuntimeException();
      }

      for (double[] vertex : vertices)
      {
         FramePoint2d testPoint = new FramePoint2d(zUpFrame, vertex);
         if (!polygon.isPointInside(testPoint))
            throw new RuntimeException();
      }

      for (double[] boundaryPoint : boundaryPoints)
      {
         FramePoint2d testPoint = new FramePoint2d(zUpFrame, boundaryPoint);
         if (!polygon.isPointInside(testPoint))
            throw new RuntimeException();
      }
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testTiming()
   {
      double[][] vertices = new double[][] { { 0.0, 0.0 }, { 1.0, 0.0 }, { 0.0, 1.0 }, { 1.0, 1.0 }, { 1.7, 0.5 }, { -0.6, 1.2 }, { 1.8, 1.1 }, { 0.5, 0.5 },
            { 0.2, 1.56 } };
      ReferenceFrame zUpFrame = ReferenceFrame.constructARootFrame("someFrame", true, false, true);

      int numberOfTests = 100000;

      long startTime = System.currentTimeMillis();
      for (int i = 0; i < numberOfTests; i++)
      {
         @SuppressWarnings("unused")
         FrameConvexPolygon2d polygon = ConvexPolygon2dTestHelpers.constructPolygon(zUpFrame, vertices);
      }

      long endTime = System.currentTimeMillis();

      double totalTime = (endTime - startTime) * 0.001;

      double timePerTest = totalTime / ((double) numberOfTests);

      System.out.println("Finding a convex polygon with " + vertices.length + " points to check took " + timePerTest * 1000.0 + " milliseconds per test.");
      double maxTimeAllowed = 0.1 * 0.001;

      assertTrue(timePerTest < maxTimeAllowed);

      FrameConvexPolygon2d polygon = ConvexPolygon2dTestHelpers.constructPolygon(zUpFrame, vertices);
      FramePoint2d pointToTest = new FramePoint2d(zUpFrame, 0.5, 0.5);

      numberOfTests = 100000;
      startTime = System.currentTimeMillis();

      for (int i = 0; i < numberOfTests; i++)
      {
         @SuppressWarnings("unused")
         boolean isInside = polygon.isPointInside(pointToTest);
      }

      endTime = System.currentTimeMillis();

      totalTime = (endTime - startTime) * 0.001;

      timePerTest = totalTime / ((double) numberOfTests);

      System.out.println("Checking inside took " + timePerTest * 1000.0 + " milliseconds per test.");
      maxTimeAllowed = 0.0025 * 0.001;

      assertTrue(timePerTest < maxTimeAllowed);
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testTimingTwo()
   {
      Random random = new Random(1776L);

      ReferenceFrame zUpFrame = ReferenceFrame.constructARootFrame("someFrame", true, false, true);

      double xMin = -1.0, xMax = 1.0, yMin = -1.0, yMax = 1.0;
      int numberOfPoints = 1000;

      ArrayList<FramePoint2d> vertices = ConvexPolygon2dTestHelpers.generateRandomCircularFramePoints(random, zUpFrame, xMin, xMax, yMin, yMax, numberOfPoints);

      int numberOfTests = 100;

      long startTime = System.currentTimeMillis();
      for (int i = 0; i < numberOfTests; i++)
      {
         @SuppressWarnings("unused")
         FrameConvexPolygon2d polygon = new FrameConvexPolygon2d(vertices);
      }

      long endTime = System.currentTimeMillis();

      double totalTime = (endTime - startTime) * 0.001;

      double timePerTest = totalTime / ((double) numberOfTests);

      System.out.println("Finding a convex polygon with " + vertices.size() + " points to check took " + timePerTest * 1000.0 + " milliseconds per test.");
      double maxTimeAllowed = 15 * 0.001;

      if (timePerTest > maxTimeAllowed)
         throw new RuntimeException();

      FrameConvexPolygon2d polygon = new FrameConvexPolygon2d(vertices);

      numberOfTests = 1000000;
      startTime = System.currentTimeMillis();

      for (int i = 0; i < numberOfTests; i++)
      {
         FramePoint2d pointToTest = FramePoint2d.generateRandomFramePoint2d(random, zUpFrame, 2.0 * xMin, 2.0 * xMax, 2.0 * yMin, 2.0 * yMax);
         @SuppressWarnings("unused")
         boolean isInside = polygon.isPointInside(pointToTest);
      }

      endTime = System.currentTimeMillis();

      totalTime = (endTime - startTime) * 0.001;

      timePerTest = totalTime / ((double) numberOfTests);

      System.out.println("Checking inside took " + timePerTest * 1000.0 + " milliseconds per test.");
      maxTimeAllowed = 0.003 * 0.001;

      if (timePerTest > maxTimeAllowed)
         throw new RuntimeException();
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testExtremePointsRandom()
   {
      Random random = new Random(1776L);

      // Create a polygon from 50 random points:
      ReferenceFrame zUpFrame = ReferenceFrame.constructARootFrame("someFrame", true, false, true);
      double xMin = -.1;
      double xMax = 0.1;
      double yMin = -0.1;
      double yMax = 0.1;

      ArrayList<FramePoint2d> randomPointList = ConvexPolygon2dTestHelpers.generateRandomRectangularFramePoints(random, zUpFrame, xMin, xMax, yMin, yMax, 50);
      FrameConvexPolygon2d randomPolygon = new FrameConvexPolygon2d(randomPointList);

      // Compute the extreme points:
      FramePoint2d frontmostLeft = randomPolygon.getMinXMaxYPointCopy();
      FramePoint2d frontmostRight = randomPolygon.getMaxXMaxYPointCopy();
      FramePoint2d backmostRight = randomPolygon.getMaxXMinYPointCopy();
      FramePoint2d backmostLeft = randomPolygon.getMinXMinYPointCopy();

      for (int i = 0; i < randomPolygon.getNumberOfVertices(); i++)
      {
         FramePoint2d point = randomPolygon.getFrameVertexCopy(i);
         assertFalse("frontmostLeft wrong, frontMostLeft = " + frontmostLeft + ", point = " + point,
               ((point.getX() < frontmostLeft.getX()) || ((point.getX() == frontmostLeft.getX()) && (point.getY() > frontmostLeft.getY()))));
         assertFalse("frontmostRight wrong, frontmostRight = " + frontmostRight + ", point = " + point,
               ((point.getX() > frontmostRight.getX()) || ((point.getX() == frontmostRight.getX()) && (point.getY() > frontmostRight.getY()))));
         assertFalse("backmostRight wrong, backmostRight = " + backmostRight + ", point = " + point,
               ((point.getX() > backmostRight.getX()) || ((point.getX() == backmostRight.getX()) && (point.getY() < backmostRight.getY()))));
         assertFalse("backmostLeft wrong, backmostLeft = " + backmostLeft + ", point = " + point,
               ((point.getX() < backmostLeft.getX()) || ((point.getX() == backmostLeft.getX()) && (point.getY() < backmostLeft.getY()))));
      }

      if (PLOT_RESULTS)
      {
         // Plot:
         FrameGeometryTestFrame testFrame = new FrameGeometryTestFrame(xMin, xMax, yMin, yMax);
         FrameGeometry2dPlotter plotter = testFrame.getFrameGeometry2dPlotter();
         plotter.setDrawPointsLarge();
         plotter.addPolygon(randomPolygon);
         plotter.addFramePoint2d(frontmostLeft, Color.green);
         plotter.addFramePoint2d(frontmostRight, Color.orange);
         plotter.addFramePoint2d(backmostRight, Color.red);
         plotter.addFramePoint2d(backmostLeft, Color.black);
         waitForButtonOrPause(testFrame);
      }
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testOrthogonalProjectionOne()
   {
      double[][] vertices = new double[][] { { 0.0, 0.0 }, { 1.0, 0.0 }, { 0.0, 1.0 }, { 1.0, 1.0 } };
      ReferenceFrame zUpFrame = ReferenceFrame.constructARootFrame("someFrame", true, false, true);

      FrameConvexPolygon2d polygon = ConvexPolygon2dTestHelpers.constructPolygon(zUpFrame, vertices);
      ConvexPolygon2dTestHelpers.verifyPointsAreClockwise(polygon);

      double[][] pointsAndSolutionToTest = new double[][] {
            { 0.5, -0.5, 0.5, 0.0 }, // Straight toward the heart.
            { 1.5, 0.5, 1.0, 0.5 }, { 0.5, 1.5, 0.5, 1.0 }, { -0.5, 0.5, 0.0, 0.5 },
            { -0.5, -0.5, 0.0, 0.0 }, // Diagonal to a vertex.
            { 1.5, -0.5, 1.0, 0.0 }, { 1.5, 1.5, 1.0, 1.0 }, { -0.5, 1.5, 0.0, 1.0 },
            { 0.0, -0.5, 0.0, 0.0 }, // Along an edge vertex normal vector.
            { -0.5, 0.0, 0.0, 0.0 }, { 1.0, -0.5, 1.0, 0.0 }, { 1.5, 0.0, 1.0, 0.0 }, { 1.0, 1.5, 1.0, 1.0 }, { 1.5, 1.0, 1.0, 1.0 }, { 0.0, 1.5, 0.0, 1.0 },
            { -0.5, 1.0, 0.0, 1.0 }, { 0.5, 0.5, 0.5, 0.5 }, // inside the Polygon. Should return the testPoint itself.
            { 0.25, 0.25, 0.25, 0.25 }, { 0.65, 0.9, 0.65, 0.9 }, };

      ArrayList<FrameLineSegment2d> projections = new ArrayList<FrameLineSegment2d>();
      ArrayList<FramePoint2d> notOutsidePoints = new ArrayList<FramePoint2d>();

      for (double[] pointAndSolutionToTest : pointsAndSolutionToTest)
      {
         FramePoint2d pointToTest2d = new FramePoint2d(zUpFrame, pointAndSolutionToTest[0], pointAndSolutionToTest[1]);
         FramePoint2d expectedSolution2d = new FramePoint2d(zUpFrame, pointAndSolutionToTest[2], pointAndSolutionToTest[3]);

         FramePoint2d closestPoint = polygon.orthogonalProjectionCopy(pointToTest2d);
         ConvexPolygon2dTestHelpers.verifyOrthogonalProjection(polygon, pointToTest2d, closestPoint);

         if (closestPoint.epsilonEquals(pointToTest2d, 1e-7))
         {
            notOutsidePoints.add(pointToTest2d);
            if (!polygon.isPointInside(pointToTest2d))
               throw new RuntimeException("Point is outside, yet projection was itself!!");
         }

         else
         {
            projections.add(new FrameLineSegment2d(pointToTest2d, closestPoint));

            // verify something!
            if (polygon.isPointInside(pointToTest2d))
               throw new RuntimeException("Point is inside, yet found a projection!");
            verifyEpsilonEquals(closestPoint, expectedSolution2d);

         }
      }

      if (PLOT_RESULTS)
      {
         FrameGeometryTestFrame testFrame = new FrameGeometryTestFrame(-1.0, 2.0, -1.0, 2.0);

         FrameGeometry2dPlotter plotter = testFrame.getFrameGeometry2dPlotter();
         plotter.setPolygonToCheckInside(polygon);

         plotter.addFrameLineSegments2d(projections, Color.green);

         testFrame.addTestPoints(notOutsidePoints);
         waitForButtonOrPause(testFrame);
      }
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testOrthogonalProjectionThree()
   {
      Random random = new Random(1776L);

      ReferenceFrame zUpFrame = ReferenceFrame.constructARootFrame("someFrame", true, false, true);
      double xMin = -100.0, xMax = 100.0, yMin = -100.0, yMax = 100.0;

      ArrayList<FramePoint2d> points = ConvexPolygon2dTestHelpers.generateRandomCircularFramePoints(random, zUpFrame, xMin, xMax, yMin, yMax, 20);

      FrameConvexPolygon2d polygon = new FrameConvexPolygon2d(points);
      ConvexPolygon2dTestHelpers.verifyPointsAreClockwise(polygon);

      // Generate random points and test the projectsions:

      int numberOfTests = 1000;

      ArrayList<FramePoint2d> originalPoints = new ArrayList<FramePoint2d>();
      ArrayList<FramePoint2d> projectedPoints = new ArrayList<FramePoint2d>();
      ArrayList<FramePoint2d> projectedPointsThatAreStillOutside = new ArrayList<FramePoint2d>();

      ArrayList<FrameLineSegment2d> goodProjectionSegments = new ArrayList<FrameLineSegment2d>();
      ArrayList<FrameLineSegment2d> badProjectionSegments = new ArrayList<FrameLineSegment2d>();

      for (int i = 0; i < numberOfTests; i++)
      {
         FramePoint2d testPoint = FramePoint2d.generateRandomFramePoint2d(random, zUpFrame, 2.0 * xMin, 2.0 * xMax, 2.0 * yMin, 2.0 * yMax);
         originalPoints.add(testPoint);

         FramePoint2d projectedPoint = polygon.orthogonalProjectionCopy(testPoint);
         ConvexPolygon2dTestHelpers.verifyOrthogonalProjection(polygon, testPoint, projectedPoint);

         boolean isInside = polygon.isPointInside(projectedPoint);

         if (isInside)
         {
            projectedPoints.add(projectedPoint);

            if (testPoint.distance(projectedPoint) > 1e-7)
            {
               FrameLineSegment2d goodSegment = new FrameLineSegment2d(testPoint, projectedPoint);
               goodProjectionSegments.add(goodSegment);
            }
         }
         else
         {
            projectedPointsThatAreStillOutside.add(projectedPoint);

            if (testPoint.distance(projectedPoint) > 1e-7)
            {
               FrameLineSegment2d badSegment = new FrameLineSegment2d(testPoint, projectedPoint);
               badProjectionSegments.add(badSegment);
            }
         }
      }

      if (!projectedPointsThatAreStillOutside.isEmpty())
         throw new RuntimeException("Some projected points are still on the outside of the polygon!");

      if (PLOT_RESULTS)
      {
         FrameGeometryTestFrame testFrame = new FrameGeometryTestFrame(xMin, xMax, yMin, yMax);

         FrameGeometry2dPlotter plotter = testFrame.getFrameGeometry2dPlotter();
         plotter.setPolygonToCheckInside(polygon);
         plotter.setDrawPointsLarge();

         plotter.addFramePoints2d(originalPoints, Color.blue);
         plotter.addFramePoints2d(projectedPoints, Color.green);
         plotter.addFramePoints2d(projectedPointsThatAreStillOutside, Color.red);

         plotter.addFrameLineSegments2d(goodProjectionSegments, Color.green);
         plotter.addFrameLineSegments2d(badProjectionSegments, Color.red);

         waitForButtonOrPause(testFrame);
      }

   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testGetAroundCornerEdgesOne()
   {
      Random random = new Random(1999L);

      ReferenceFrame zUpFrame = ReferenceFrame.constructARootFrame("someFrame", true, false, true);
      double xMin = -100.0, xMax = 100.0, yMin = -100.0, yMax = 100.0;

      ArrayList<FramePoint2d> points = ConvexPolygon2dTestHelpers.generateRandomCircularFramePoints(random, zUpFrame, xMin, xMax, yMin, yMax, 30);

      FrameConvexPolygon2d polygon = new FrameConvexPolygon2d(points);
      ConvexPolygon2dTestHelpers.verifyPointsAreClockwise(polygon);

      FrameGeometry2dPlotter plotter = null;
      FrameGeometryTestFrame testFrame = null;
      if (PLOT_RESULTS)
      {
         testFrame = new FrameGeometryTestFrame(xMin, xMax, yMin, yMax);

         plotter = testFrame.getFrameGeometry2dPlotter();
         plotter.setPolygonToCheckInside(polygon);

         testFrame.addTestPoints(points);
      }

      int numAroundCornerTests = 100000;

      ArrayList<FramePoint2d> randomOutsidePoints = new ArrayList<FramePoint2d>();

      for (int i = 0; i < numAroundCornerTests; i++)
      {
         FramePoint2d randomPoint = FramePoint2d.generateRandomFramePoint2d(random, zUpFrame, 2.0 * xMin, 2.0 * xMax, 2.0 * yMin, 2.0 * yMax);
         if (!polygon.isPointInside(randomPoint))
         {
            randomOutsidePoints.add(randomPoint);

            FramePoint2d[] lineOfSightVertices = polygon.getLineOfSightVertices(randomPoint);
            FrameLineSegment2d[] aroundCornerEdges = polygon.getAroundTheCornerEdges(randomPoint);

            ConvexPolygon2dTestHelpers.verifyAroundTheCornerEdges(polygon, randomPoint, lineOfSightVertices, aroundCornerEdges);

            if (PLOT_RESULTS)
            {
               FrameLineSegment2d frameLine1 = new FrameLineSegment2d(randomPoint, lineOfSightVertices[0]);
               FrameLineSegment2d frameLine2 = new FrameLineSegment2d(randomPoint, lineOfSightVertices[1]);

               plotter.addFrameLineSegment2d(frameLine1, Color.cyan);
               plotter.addFrameLineSegment2d(aroundCornerEdges[0], Color.cyan);

               plotter.addFrameLineSegment2d(frameLine2, Color.magenta);
               plotter.addFrameLineSegment2d(aroundCornerEdges[1], Color.magenta);
            }

         }
         else
         {
         }
      }

      if (PLOT_RESULTS)
      {
         waitForButtonOrPause(testFrame);
      }

   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testGetLineOfSightVerticesOne()
   {
      double[][] vertices = new double[][] { { 0.0, 0.0 }, { 1.0, 0.0 }, { 0.0, 1.0 }, { 1.0, 1.0 } };
      ReferenceFrame zUpFrame = ReferenceFrame.constructARootFrame("someFrame", true, false, true);

      FrameConvexPolygon2d polygon = ConvexPolygon2dTestHelpers.constructPolygon(zUpFrame, vertices);

      double[][] pointsToTest = new double[][] { { 0.5, -0.5 }, { 1.5, 0.5 }, { 0.5, 1.5 }, { -0.5, 0.5 }, { -0.5, -0.5 }, { 1.5, -0.5 }, { 1.5, 1.5 },
            { -0.5, 1.5 } };

      for (double[] pointToTestDoubles : pointsToTest)
      {
         FramePoint2d pointToTest = new FramePoint2d(zUpFrame, pointToTestDoubles);
         performLineOfSightTest(polygon, pointToTest);
      }

   }

	@DeployableTestMethod(estimatedDuration = 0.6)
	@Test(timeout = 30000)
   public void testGetLineOfSightVerticesTwo()
   {
      Random random = new Random(1092L);

      ReferenceFrame zUpFrame = ReferenceFrame.constructARootFrame("someFrame", true, false, true);
      double xMin = -100.0, xMax = 100.0, yMin = -100.0, yMax = 100.0;

      ArrayList<FramePoint2d> points = ConvexPolygon2dTestHelpers.generateRandomCircularFramePoints(random, zUpFrame, xMin, xMax, yMin, yMax, 200);

      FrameConvexPolygon2d polygon = new FrameConvexPolygon2d(points);
      ConvexPolygon2dTestHelpers.verifyPointsAreClockwise(polygon);

      FrameGeometryTestFrame testFrame = null;
      FrameGeometry2dPlotter plotter = null;

      if (PLOT_RESULTS)
      {
         testFrame = new FrameGeometryTestFrame(xMin, xMax, yMin, yMax);

         plotter = testFrame.getFrameGeometry2dPlotter();
         plotter.setPolygonToCheckInside(polygon);

         testFrame.addTestPoints(points);
      }

      int numLineOfSightTests = 100000;

      ArrayList<FramePoint2d> randomOutsidePoints = new ArrayList<FramePoint2d>();

      for (int i = 0; i < numLineOfSightTests; i++)
      {
         FramePoint2d randomPoint = FramePoint2d.generateRandomFramePoint2d(random, zUpFrame, 2.0 * xMin, 2.0 * xMax, 2.0 * yMin, 2.0 * yMax);
         if (!polygon.isPointInside(randomPoint))
         {
            randomOutsidePoints.add(randomPoint);

            FramePoint2d[] lineOfSightVertices = polygon.getLineOfSightVertices(randomPoint);
            ConvexPolygon2dTestHelpers.verifyLineOfSightVertices(polygon, randomPoint, lineOfSightVertices);

            FrameLine2d frameLine1 = new FrameLine2d(randomPoint, lineOfSightVertices[0]);
            FrameLine2d frameLine2 = new FrameLine2d(randomPoint, lineOfSightVertices[1]);

            if (PLOT_RESULTS)
            {
               plotter.addFrameLine2d(frameLine1, Color.cyan);
               plotter.addFrameLine2d(frameLine2, Color.magenta);
            }
         }
         else
         {
         }
      }

      System.gc();
      try
      {
         Thread.sleep(100);
      }
      catch (InterruptedException e)
      {
      }
      long startTime = System.currentTimeMillis();
      for (FramePoint2d testPoint : randomOutsidePoints)
      {
         polygon.getLineOfSightVertices(testPoint);
      }

      long endTime = System.currentTimeMillis();

      double totalTime = (endTime - startTime) * 0.001;

      int numberOfLineOfSightTests = randomOutsidePoints.size();
      double millisPerTest = totalTime / ((double) numberOfLineOfSightTests) * 1000.0;

      System.out.println("Performed " + numberOfLineOfSightTests + " line of sight tests in " + totalTime + " seconds. Or " + millisPerTest
            + " milliseconds per test, with a polygon with " + polygon.getNumberOfVertices() + " vertices.");

      assertTrue(millisPerTest < 0.002);

      if (PLOT_RESULTS)
      {
         waitForButtonOrPause(testFrame);
      }
   }
   
   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testPolygonShrinkInto()
   {
      Random random = new Random(2002L);

      ReferenceFrame zUpFrame = ReferenceFrame.constructARootFrame("someFrame", true, false, true);

      double xMin = 0.0, xMax = 1.0, yMin = 0.0, yMax = 1.0;
      double widthMax = 0.5, heightMax = 0.5;
      int numberOfPoints = 20;
      int numberOfPolygons = 30;

      ConvexPolygon2d randomPPolygon = ConvexPolygon2dTestHelpers.generateRandomPolygon(random, zUpFrame, -0.1, 0.1, -0.1, 0.1, 5).getConvexPolygon2dCopy();
      ArrayList<FrameConvexPolygon2d> randomQPolygons = ConvexPolygon2dTestHelpers.generateRandomPolygons(random, zUpFrame, xMin, xMax, yMin, yMax, widthMax,
            heightMax, numberOfPoints, numberOfPolygons);

      // Find the matrix of intersecting polygons:
      ConvexPolygon2d[] shrunkenPolygons = new ConvexPolygon2d[randomQPolygons.size()];
      Point2d referencePointForP = randomPPolygon.getCentroid();

      for (int j = 0; j < randomQPolygons.size(); j++)
      {
         FrameConvexPolygon2d polygonQ = randomQPolygons.get(j);

         ConvexPolygon2d convexPolygonQ = polygonQ.getConvexPolygon2dCopy();

         try
         {
            shrunkenPolygons[j] = ConvexPolygonTools.shrinkInto(randomPPolygon, referencePointForP, convexPolygonQ);
         }
         catch (RuntimeException exception)
         {
         }

      }

      FrameGeometryTestFrame testFrame = null;
      FrameGeometry2dPlotter plotter = null;

      if (PLOT_RESULTS)
      {
         testFrame = new FrameGeometryTestFrame(xMin, xMax, yMin, yMax);
         plotter = testFrame.getFrameGeometry2dPlotter();

         plotter.addFrameConvexPolygons(randomQPolygons, Color.CYAN);
         plotter.addConvexPolygons(shrunkenPolygons, Color.BLACK);

         plotter.repaint();
      }

      // Generate a bunch of points. For each one, if it is in the shrunken polygon, make sure P moved to the point is fully inside Q.
      // If not, make sure that P moved to the point is not fully inside Q.

      ArrayList<FramePoint2d> testPoints = ConvexPolygon2dTestHelpers.generateRandomRectangularFramePoints(random, zUpFrame, xMin, xMax, yMin, yMax, 10000);

      for (FramePoint2d testPoint : testPoints)
      {
         boolean buggy = false;
         boolean insideAnyShrunkenPolygon = false;

         for (int j = 0; j < randomQPolygons.size(); j++)
         {
            ConvexPolygon2d polygonQ = randomQPolygons.get(j).getConvexPolygon2dCopy();

            ConvexPolygon2d shrunkenPolygon = shrunkenPolygons[j];

            boolean insideShrunkenPolygon = ((shrunkenPolygon != null) && shrunkenPolygon.isPointInside(testPoint.getPointCopy()));
            if (insideShrunkenPolygon)
               insideAnyShrunkenPolygon = true;

            // If point is inside, then polygonP when moved to this location should be fully inside Q.
            // Otherwise it shouldn't be fully inside Q.

            Vector2d translation = new Vector2d(testPoint.getPointCopy());
            translation.sub(referencePointForP);
            ConvexPolygon2d translatedPolygon = randomPPolygon.translateCopy(translation);

            boolean completelyInside = translatedPolygon.isCompletelyInside(polygonQ);

            if (insideShrunkenPolygon && !completelyInside)
            {
               buggy = true;
               String errorMessage = "\n\ninsideShrunkenPolygon && !completelyInside. \nrandomPPolygon = " + randomPPolygon + ", \npolygonQ = " + polygonQ
                     + ", \ntestPoint = " + testPoint;
               System.err.println(errorMessage);

               throw new RuntimeException(errorMessage);
            }

            if (!insideShrunkenPolygon && completelyInside)
            {
               buggy = true;
               String errorMessage = "!insideShrunkenPolygon && completelyInside. \nrandomPPolygon = " + randomPPolygon + ", \npolygonQ = " + polygonQ
                     + ", \ntestPoint = " + testPoint;
               System.err.println(errorMessage);

               throw new RuntimeException(errorMessage);
            }
         }

         if (PLOT_RESULTS)
         {
            if (buggy)
            {
               plotter.addFramePoint2d(testPoint, Color.RED);
            }

            if (insideAnyShrunkenPolygon)
            {
               plotter.addFramePoint2d(testPoint, Color.GREEN);
            }
            else
            {
               plotter.addFramePoint2d(testPoint, Color.GRAY);
            }
         }

      }

      if (PLOT_RESULTS)
      {
         waitForButtonOrPause(testFrame);
      }
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testPolygonIntersections()
   {
      Random random = new Random(1886L);

      ReferenceFrame zUpFrame = ReferenceFrame.constructARootFrame("someFrame", true, false, true);

      double xMin = 0.0, xMax = 1.0, yMin = 0.0, yMax = 1.0;
      double widthMax = 0.5, heightMax = 0.5;
      int numberOfPoints = 20;
      int numberOfPolygons = 30;

      ArrayList<FrameConvexPolygon2d> randomPolygons = ConvexPolygon2dTestHelpers.generateRandomPolygons(random, zUpFrame, xMin, xMax, yMin, yMax, widthMax,
            heightMax, numberOfPoints, numberOfPolygons);

      FrameGeometryTestFrame testFrame = null;
      FrameGeometry2dPlotter plotter = null;

      if (PLOT_RESULTS)
      {
         testFrame = new FrameGeometryTestFrame(xMin, xMax, yMin, yMax);
         plotter = testFrame.getFrameGeometry2dPlotter();
         plotter.setDrawPointsMedium();
      }

      // Find the matrix of intersecting polygons:
      ConvexPolygon2d[][] intersectingPolygons = new ConvexPolygon2d[randomPolygons.size()][randomPolygons.size()];

      int n = randomPolygons.size();
      for (int i = 0; i < n; i++)
      {
         for (int j = 0; j < n; j++)
         {
            FrameConvexPolygon2d polygon1 = randomPolygons.get(i);
            FrameConvexPolygon2d polygon2 = randomPolygons.get(j);

            ConvexPolygon2d convexPolygon1 = polygon1.getConvexPolygon2dCopy();
            ConvexPolygon2d convexPolygon2 = polygon2.getConvexPolygon2dCopy();

            ConvexPolygon2d intersectingPolygon = new ConvexPolygon2d();
            boolean success = ConvexPolygonTools.computeIntersectionOfPolygons(convexPolygon1, convexPolygon2, intersectingPolygon);
            if (!success) intersectingPolygon = null;
            intersectingPolygons[i][j] = intersectingPolygon;

            if ((success) && (i != j))
            {
               if (PLOT_RESULTS)
               {
                  plotter.addPolygon(new FrameConvexPolygon2d(zUpFrame, intersectingPolygon), Color.BLACK);
                  plotter.repaint();
               }
            }
         }
      }

      if (PLOT_RESULTS)
      {
         plotter.addFrameConvexPolygons(randomPolygons, Color.CYAN);
         plotter.repaint();
      }

      // Generate a bunch of points. For each one, if it is in the intersection of any intersecting polygon, make sure it is in both of the polygon's parents:
      ArrayList<FramePoint2d> testPoints = ConvexPolygon2dTestHelpers.generateRandomRectangularFramePoints(random, zUpFrame, xMin, xMax, yMin, yMax, 10000);

      for (FramePoint2d testPoint : testPoints)
      {
         boolean insideAnyIntersection = false;

         for (int i = 0; i < n; i++)
         {
            for (int j = 0; j < n; j++)
            {
               FrameConvexPolygon2d polygon1 = randomPolygons.get(i);
               FrameConvexPolygon2d polygon2 = randomPolygons.get(j);

               boolean inside1 = polygon1.isPointInside(testPoint);
               boolean inside2 = polygon2.isPointInside(testPoint);

               ConvexPolygon2d intersectionPolygon = intersectingPolygons[i][j];
               if (i == j)
               {
                  assertNotNull(intersectionPolygon);
               }

               boolean insideIntersection = ((intersectionPolygon != null) && (intersectionPolygon.isPointInside(testPoint.getPointCopy())));
               if (insideIntersection)
               {
                  insideAnyIntersection = true;
               }

               if (inside1 && inside2)
               {
                  assertTrue("inside1 and inside2, but not inside intersection", insideIntersection);
               }

               if (insideIntersection)
               {
                  assertTrue("insideIntersection, but not inside1", inside1);
                  assertTrue("insideIntersection, but not inside2", inside2);
               }
            }
         }

         if (PLOT_RESULTS)
         {
            if (insideAnyIntersection)
            {
               plotter.addFramePoint2d(testPoint, Color.GREEN);
            }
            else
            {
               plotter.addFramePoint2d(testPoint, Color.GRAY);
            }
         }

      }

      if (PLOT_RESULTS)
      {
         plotter.repaint();
         waitForButtonOrPause(testFrame);
      }
   }

   private void performLineOfSightTest(FrameConvexPolygon2d polygon, FramePoint2d pointToTest)
   {
      FramePoint2d[] lineOfSightVertices = polygon.getLineOfSightVertices(pointToTest);
      ConvexPolygon2dTestHelpers.verifyLineOfSightVertices(polygon, pointToTest, lineOfSightVertices);
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testPullTowardsCentroid()
   {
      Random random = new Random(2002L);

      ReferenceFrame zUpFrame = ReferenceFrame.constructARootFrame("someFrame", true, false, true);
      double xMin = -.1;
      double xMax = 0.1;
      double yMin = -0.1;
      double yMax = 0.1;

      ArrayList<FramePoint2d> randomPointList = ConvexPolygon2dTestHelpers.generateRandomRectangularFramePoints(random, zUpFrame, xMin, xMax, yMin, yMax, 10);
      FrameConvexPolygon2d randomPolygon = new FrameConvexPolygon2d(randomPointList);
      FramePoint2d centroid = randomPolygon.getCentroidCopy();

      FrameGeometryTestFrame testFrame = null;
      FrameGeometry2dPlotter plotter = null;
      if (PLOT_RESULTS)
      {
         testFrame = new FrameGeometryTestFrame(xMin, xMax, yMin, yMax);
         plotter = testFrame.getFrameGeometry2dPlotter();
         plotter.addPolygon(randomPolygon);
         plotter.addFramePoint2d(centroid, Color.blue);
      }

      int numberOfTestPoints = 100;
      for (int i = 0; i < numberOfTestPoints; i++)
      {
         double percentToPull = 0.20;

         FramePoint2d testPoint = FramePoint2d.generateRandomFramePoint2d(random, zUpFrame, xMin, xMax, yMin, yMax);
         FramePoint2d pulledTestPoint = new FramePoint2d(testPoint);
         randomPolygon.pullPointTowardsCentroid(pulledTestPoint, percentToPull);

         double distanceFromCentroidToTestPoint = testPoint.distance(centroid);
         double distanceFromCentroidToPulledPoint = pulledTestPoint.distance(centroid);
         assertTrue(distanceFromCentroidToPulledPoint < distanceFromCentroidToTestPoint);
         double measuredPercentPulled = 1.0 - distanceFromCentroidToPulledPoint / distanceFromCentroidToTestPoint;
         assertEquals(percentToPull, measuredPercentPulled, 0.001);

         assertTrue(GeometryTools.arePointsInOrderAndColinear(testPoint.getPoint(), pulledTestPoint.getPoint(), centroid.getPoint(), 1e-10));

         if (PLOT_RESULTS)
         {
            plotter.addFramePoint2d(testPoint, Color.green);
            plotter.addFramePoint2d(pulledTestPoint, Color.black);
            plotter.addFrameLineSegment2d(new FrameLineSegment2d(testPoint, pulledTestPoint), Color.cyan);
         }
      }

      if (PLOT_RESULTS)
      {
         waitForButtonOrPause(testFrame);
      }
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testGetNearestVertex()
   {
      Random random = new Random(1776L);

      ReferenceFrame zUpFrame = ReferenceFrame.constructARootFrame("someFrame", true, false, true);
      double xMin = -100.0, xMax = 100.0, yMin = -100.0, yMax = 100.0;

      ArrayList<FramePoint2d> points = ConvexPolygon2dTestHelpers.generateRandomCircularFramePoints(random, zUpFrame, xMin, xMax, yMin, yMax, 20);

      FrameConvexPolygon2d polygon = new FrameConvexPolygon2d(points);
      ConvexPolygon2dTestHelpers.verifyPointsAreClockwise(polygon);

      // Generate random lines and test the nearest vertices:
      int numberOfTests = 100;

      ArrayList<FrameLine2d> originalLines = new ArrayList<FrameLine2d>();
      ArrayList<FramePoint2d> nearestVertices = new ArrayList<FramePoint2d>();
      ArrayList<FrameLineSegment2d> orthogonalSegments = new ArrayList<FrameLineSegment2d>();

      for (int i = 0; i < numberOfTests; i++)
      {
         FrameLine2d testLine = FrameLine2d.generateRandomFrameLine2d(random, zUpFrame, 2.0 * xMin, 2.0 * xMax, 2.0 * yMin, 2.0 * yMax);
         originalLines.add(testLine);
         originalLines.add(testLine.negateDirectionCopy());

         FramePoint2d nearestVertex = polygon.getClosestVertexCopy(testLine);
         nearestVertices.add(nearestVertex);

         FramePoint2d orthogonalPoint = testLine.orthogonalProjectionCopy(nearestVertex);
         FrameLineSegment2d orthogonalSegment = new FrameLineSegment2d(nearestVertex, orthogonalPoint);
         orthogonalSegments.add(orthogonalSegment);

         if (!polygon.isPointInside(orthogonalPoint))
         {
            // Test: Orthogonal point should be closer to the nearestVertex, then to the two connected vertices:
            // But only do it for points that are outside the Polygon. Otherwise, there are no nearest edges to a point inside.
            FrameLineSegment2d[] nearestEdges = polygon.getNearestEdges(orthogonalPoint);
            double minimumDistanceToEdgeVertices = Double.POSITIVE_INFINITY;

            for (FrameLineSegment2d nearEdge : nearestEdges)
            {
               for (FramePoint2d nearVertex : nearEdge.getEndFramePointsCopy())
               {
                  double distance = nearVertex.distance(orthogonalPoint);
                  if (distance < minimumDistanceToEdgeVertices)
                  {
                     minimumDistanceToEdgeVertices = distance;
                  }
               }
            }

            assertEquals(minimumDistanceToEdgeVertices, orthogonalPoint.distance(nearestVertex), 1e-10);
         }
      }

      if (PLOT_RESULTS)
      {
         FrameGeometryTestFrame testFrame = new FrameGeometryTestFrame(xMin, xMax, yMin, yMax);

         FrameGeometry2dPlotter plotter = testFrame.getFrameGeometry2dPlotter();
         plotter.setPolygonToCheckInside(polygon);

         plotter.addFrameLines2d(originalLines, Color.blue);
         plotter.addFramePoints2d(nearestVertices, Color.green);
         plotter.addFrameLineSegments2d(orthogonalSegments, Color.green);

         waitForButtonOrPause(testFrame);
      }
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testTroublesomeIntersection()
   {
      double[][] vertices = new double[][] { { 0.0, 0.0 }, { 1.0, 0.0 }, { 0.0, 1.0 }, { 1.0, 1.0 } };

      ReferenceFrame worldFrame = ReferenceFrame.getWorldFrame();
      FrameConvexPolygon2d polygon = new FrameConvexPolygon2d(worldFrame, vertices);

      FrameLine2d line = new FrameLine2d(worldFrame, new Point2d(1.0, 0.5), new Point2d(1.5, 0.5));
      FramePoint2d[] intersections = polygon.intersectionWith(line);

      assertEquals(2, intersections.length);
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testIntersectionWithLinesOne()
   {
      double[][] vertices = new double[][] { { 0.0, 0.0 }, { 1.0, 0.0 }, { 0.0, 1.0 }, { 1.0, 1.0 } };
      ReferenceFrame zUpFrame = ReferenceFrame.constructARootFrame("someFrame", true, false, true);

      FrameConvexPolygon2d polygon = ConvexPolygon2dTestHelpers.constructPolygon(zUpFrame, vertices);
      ConvexPolygon2dTestHelpers.verifyPointsAreClockwise(polygon);

      double[][] pointsAndLinesToTest = new double[][] {
            { 0.5, -0.5, 0.0, 1.0 }, // Straight through the heart.
            { 1.5, 0.5, -1.0, 0.0 },
            { 0.5, 1.5, 0.0, -1.0 },
            { -0.5, 0.5, 1.0, 0.0 },
            { -0.5, -0.5, 1.0, 1.0 }, // Diagonal through two vertices.
            { 1.5, -0.5, -1.0, 1.0 },
            { 1.5, 1.5, -1.0, -1.0 },
            { -0.5, 1.5, 1.0, -1.0 },
            { -0.5, -0.5, 0.0, 1.0 }, // Don't intersect at all.
            { -0.5, -0.5, 1.0, 0.0 }, { 1.5, -0.5, -1.0, 0.0 }, { 1.5, -0.5, 0.0, 1.0 }, { 1.5, 1.5, -1.0, 0.0 }, { 1.5, 1.5, 0.0, -1.0 },
            { -0.5, 1.5, 0.0, -1.0 },
            { -0.5, 1.5, 1.0, 0.0 },
            { -1.0, 0.0, 1.0, 0.0 }, // Parallel to an edge. These should return null.
            { 0.0, -1.0, 0.0, 1.0 }, { 1.0, -1.0, 0.0, 1.0 }, { 2.0, 0.0, -1.0, 0.0 }, { 1.0, 2.0, 0.0, -1.0 }, { 2.0, 1.0, -1.0, 0.0 },
            { 0.0, 2.0, 0.0, -1.0 }, { -1.0, 1.0, 1.0, 0.0 },
            { 0.5, -0.5, 0.0, -1.0 }, // Line points away from Polygon. These should return null. We treat the line as a ray!
            { 1.5, 0.5, 1.0, 0.0 }, { 0.5, 1.5, 0.0, 1.0 }, { -0.5, 0.5, -1.0, 0.0 },
            { 0.5, 0.5, 0.0, 1.0 }, // Line starts inside the Polygon. Should only be one leaving point.
            { 0.5, 0.5, 0.0, -1.0 }, { 0.5, 0.5, 1.0, 0.0 }, { 0.5, 0.5, -1.0, 0.0 }, { 0.5, 0.5, 1.0, 1.0 }, { 0.5, 0.5, 1.0, -1.0 }, { 0.5, 0.5, -1.0, 1.0 },
            { 0.5, 0.5, -1.0, -1.0 }, { 1.0, 0.5, 1.5, 0.5 } };

      ArrayList<FrameLine2d> nonIntersectingLines = new ArrayList<FrameLine2d>();
      ArrayList<FrameLine2d> intersectingLines = new ArrayList<FrameLine2d>();
      ArrayList<FramePoint2d> intersectingPoints = new ArrayList<FramePoint2d>();

      for (double[] pointToTestDoubles : pointsAndLinesToTest)
      {
         FramePoint2d framePoint2d = new FramePoint2d(zUpFrame, pointToTestDoubles[0], pointToTestDoubles[1]);
         FrameVector2d frameVector2d = new FrameVector2d(zUpFrame, pointToTestDoubles[2], pointToTestDoubles[3]);

         FrameLine2d frameLine2d = new FrameLine2d(framePoint2d, frameVector2d);

         FrameLineSegment2d[] intersectingEdges = polygon.getIntersectingEdges(frameLine2d);

         if (intersectingEdges == null)
         {
            nonIntersectingLines.add(frameLine2d);
            ConvexPolygon2dTestHelpers.verifyLineDoesNotIntersectPolygon(frameLine2d, polygon);
         }

         else
         {
            intersectingLines.add(frameLine2d);
            ConvexPolygon2dTestHelpers.verifyLineIntersectsEdge(frameLine2d, intersectingEdges);
         }
      }

      if (PLOT_RESULTS)
      {
         FrameGeometryTestFrame testFrame = new FrameGeometryTestFrame(-1.0, 2.0, -1.0, 2.0);

         FrameGeometry2dPlotter plotter = testFrame.getFrameGeometry2dPlotter();
         plotter.setDrawPointsMedium();
         plotter.setPolygonToCheckInside(polygon);

         plotter.addFrameLines2d(intersectingLines, Color.green);
         plotter.addFrameLines2d(nonIntersectingLines, Color.red);

         testFrame.addTestPoints(intersectingPoints);

         waitForButtonOrPause(testFrame);
      }
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testGetOppositeMidEdgeWhenPublic()
   {
      int[][] leftRightNumExpecteds = new int[][] { { 0, 0, 6, 3 }, { 1, 1, 6, 4 }, { 2, 2, 6, 5 }, { 3, 3, 6, 0 }, { 4, 4, 6, 1 }, { 5, 5, 6, 2 },
            { 1, 0, 6, 1 }, { 2, 0, 6, 1 }, { 3, 0, 6, 2 }, { 4, 0, 6, 2 }, { 5, 0, 6, 3 }, { 2, 1, 6, 2 }, { 3, 1, 6, 2 }, { 4, 1, 6, 3 }, { 5, 1, 6, 3 },
            { 3, 2, 6, 3 }, { 4, 2, 6, 3 }, { 5, 2, 6, 4 }, { 4, 3, 6, 4 }, { 5, 3, 6, 4 }, { 5, 4, 6, 5 }, { 0, 1, 6, 4 }, { 0, 2, 6, 4 }, { 0, 3, 6, 5 },
            { 0, 4, 6, 5 }, { 0, 5, 6, 0 }, { 1, 2, 6, 5 }, { 1, 3, 6, 5 }, { 1, 4, 6, 0 }, { 1, 5, 6, 0 }, { 2, 3, 6, 0 }, { 2, 4, 6, 0 }, { 2, 5, 6, 1 },
            { 3, 4, 6, 1 }, { 3, 5, 6, 1 }, { 4, 5, 6, 2 }, { 0, 0, 3, 2 }, { 1, 1, 3, 0 }, { 2, 2, 3, 1 }, { 0, 1, 3, 2 }, { 0, 2, 3, 0 }, { 1, 2, 3, 0 },
            { 1, 0, 3, 1 }, { 2, 0, 3, 1 }, { 2, 1, 3, 2 }, };

      ConvexPolygon2d polygon = new ConvexPolygon2d();
      for (int[] leftRightNumExpected : leftRightNumExpecteds)
      {
         int numEdges = leftRightNumExpected[2];
         double radius = 1.0;

         polygon.clear();
         for (double angle = 0; angle < 2.0 * Math.PI; angle += 2.0 * Math.PI / numEdges)
         {
            double x = radius * Math.cos(angle);
            double y = radius * Math.sin(angle);
            polygon.addVertex(x, y);
         }
         polygon.update();

         int leftEdge = leftRightNumExpected[0];
         int rightEdge = leftRightNumExpected[1];
         int expectedAnswer = leftRightNumExpected[3];

         int answer = polygon.getMidEdgeOppositeClockwiseOrdering(leftEdge, rightEdge);

         if (answer != expectedAnswer)
            throw new RuntimeException("leftEdge = " + leftEdge + ", rightEdge = " + rightEdge + ", numEdges = " + numEdges + ", expectedAnswer = "
                  + expectedAnswer + ", answer = " + answer);
      }
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testCombineOne()
   {
      Random random = new Random(1992L);

      ReferenceFrame zUpFrame = ReferenceFrame.constructARootFrame("someFrame", true, false, true);
      double xMinA = -100.0, xMaxA = 100.0, yMinA = -100.0, yMaxA = 100.0;
      double xMinB = 100.0, xMaxB = 300.0, yMinB = 100.0, yMaxB = 300.0;

      double xMin = Math.min(xMinA, xMinB);
      double xMax = Math.max(xMaxA, xMaxB);
      double yMin = Math.min(yMinA, yMinB);
      double yMax = Math.max(yMaxA, yMaxB);

      ArrayList<FramePoint2d> pointsA = ConvexPolygon2dTestHelpers.generateRandomRectangularFramePoints(random, zUpFrame, xMinA, xMaxA, yMinA, yMaxA, 10000);
      ArrayList<FramePoint2d> pointsB = ConvexPolygon2dTestHelpers.generateRandomCircularFramePoints(random, zUpFrame, xMinB, xMaxB, yMinB, yMaxB, 10000);

      FrameConvexPolygon2d polygonA = new FrameConvexPolygon2d(pointsA);
      ConvexPolygon2dTestHelpers.verifyPointsAreClockwise(polygonA);

      FrameConvexPolygon2d polygonB = new FrameConvexPolygon2d(pointsB);
      ConvexPolygon2dTestHelpers.verifyPointsAreClockwise(polygonB);

      FrameConvexPolygon2d polygonC = new FrameConvexPolygon2d(polygonA, polygonB);
      ConvexPolygon2dTestHelpers.verifyPointsAreClockwise(polygonC);

      for (FramePoint2d point : pointsA)
      {
         if (!polygonC.isPointInside(point))
         {
            throw new RuntimeException();
         }
      }

      for (FramePoint2d point : pointsB)
      {
         if (!polygonC.isPointInside(point))
         {
            throw new RuntimeException();
         }
      }

      if (PLOT_RESULTS)
      {
         FrameGeometryTestFrame testFrame = new FrameGeometryTestFrame(xMin, xMax, yMin, yMax);

         FrameGeometry2dPlotter plotter = testFrame.getFrameGeometry2dPlotter();
         plotter.setPolygonToCheckInside(polygonC);

         testFrame.addTestPoints(pointsA);
         testFrame.addTestPoints(pointsB);
         waitForButtonOrPause(testFrame);
      }

   }

   private void waitForButtonOrPause(FrameGeometryTestFrame testFrame)
   {
      if (WAIT_FOR_BUTTON_PUSH)
         testFrame.waitForButtonPush();
      else
         pauseOneSecond();
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testGetOutSideFacingOrthoNormalVectors()
   {
      ConvexPolygon2d polygon = createSomeValidPolygon();

      List<Vector2d> normalVectors = polygon.getOutSideFacingOrthoNormalVectorsCopy();

      for (Vector2d normalVector : normalVectors)
      {
         assertEquals(1.0, normalVector.length(), 1e-9);
      }

      assertEquals(polygon.getNumberOfVertices(), normalVectors.size());

      double delta = 5e-3;
      for (int i = 0; i < polygon.getNumberOfVertices(); i++)
      {
         Vector2d normalVector = normalVectors.get(i);

         Point2d startPoint = polygon.getVertex(i);
         Point2d endPoint = polygon.getNextVertex(i);

         double dx = endPoint.getX() - startPoint.getX();
         double dy = endPoint.getY() - startPoint.getY();

         Vector2d lineDirection = (new Vector2d(dx, dy));
         double dot = lineDirection.dot(normalVector);
         assertEquals(0.0, dot, delta);

         // test if outsideFacing
         LineSegment2d lineSegment = new LineSegment2d(startPoint, endPoint);
         Point2d testPoint = lineSegment.midpoint();

         assertTrue(polygon.isPointInside(testPoint, 1e-15));

         normalVector.scale(1e-9);
         testPoint.add(normalVector);

         assertFalse(polygon.isPointInside(testPoint, 1e-15));
      }
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testApplyTransformWithTranslations()
   {
      Random random = new Random(1776L);
      RigidBodyTransform transform = new RigidBodyTransform();

      // pure translation:
      Vector3d translation = new Vector3d(random.nextDouble(), random.nextDouble(), 0.0);
      transform.setTranslation(translation);

      final int listSize = 50;

      ArrayList<Point2d> pointList = new ArrayList<Point2d>();
      for (int i = 0; i < listSize; i++)
      {
         pointList.add(new Point2d(random.nextDouble(), random.nextDouble()));
      }

      ConvexPolygon2d convexPolygon = new ConvexPolygon2d(pointList);
      ConvexPolygon2d returnPolygon = new ConvexPolygon2d(convexPolygon);

      returnPolygon.applyTransform(transform);

      if (PLOT_RESULTS)
      {
         ReferenceFrame worldFrame = ReferenceFrame.getWorldFrame();
         FrameGeometryTestFrame testFrame = new FrameGeometryTestFrame(-1.0, 2.0, -1.0, 2.0);
         FrameGeometry2dPlotter plotter = testFrame.getFrameGeometry2dPlotter();
         plotter.addPolygon(new FrameConvexPolygon2d(worldFrame, convexPolygon));
         plotter.addPolygon(new FrameConvexPolygon2d(worldFrame, returnPolygon));
         waitForButtonOrPause(testFrame);
      }

      for (int i = 0; i < convexPolygon.getNumberOfVertices(); i++)
      {
         assertEquals("Translation not handled correctly", convexPolygon.getVertex(i).x + translation.x, returnPolygon.getVertex(i).x, 1e-7);
         assertEquals("Translation not handled correctly", convexPolygon.getVertex(i).y + translation.y, returnPolygon.getVertex(i).y, 1e-7);
      }
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testApplyTransformWithRotation()
   {
      Random random = new Random(1984L);
      RigidBodyTransform transform = new RigidBodyTransform();

      // pure translation:
      double yaw = 0.4;
      Vector3d eulerAngles = new Vector3d(0.0, 0.0, yaw);
      transform.setEuler(eulerAngles);

      final int listSize = 8;

      ArrayList<Point2d> pointList = new ArrayList<Point2d>();
      for (int i = 0; i < listSize; i++)
      {
         pointList.add(new Point2d(random.nextDouble(), random.nextDouble()));
      }

      ConvexPolygon2d convexPolygon = new ConvexPolygon2d(pointList);
      ConvexPolygon2d returnPolygon = new ConvexPolygon2d(convexPolygon);
      returnPolygon.applyTransform(transform);

      if (PLOT_RESULTS)
      {
         ReferenceFrame worldFrame = ReferenceFrame.getWorldFrame();
         FrameGeometryTestFrame testFrame = new FrameGeometryTestFrame(-1.0, 2.0, -1.0, 2.0);
         FrameGeometry2dPlotter plotter = testFrame.getFrameGeometry2dPlotter();
         plotter.addPolygon(new FrameConvexPolygon2d(worldFrame, convexPolygon));
         plotter.addPolygon(new FrameConvexPolygon2d(worldFrame, returnPolygon));
         waitForButtonOrPause(testFrame);
      }

      for (int i = 0; i < convexPolygon.getNumberOfVertices(); i++)
      {
         double expectedX = convexPolygon.getVertex(i).x * Math.cos(yaw) - convexPolygon.getVertex(i).y * Math.sin(yaw);
         double expectedY = convexPolygon.getVertex(i).x * Math.sin(yaw) + convexPolygon.getVertex(i).y * Math.cos(yaw);

         assertEquals("Rotation not handled correctly", expectedX, returnPolygon.getVertex(i).x, 1e-7);
         assertEquals("Rotation not handled correctly", expectedY, returnPolygon.getVertex(i).y, 1e-7);
      }
   }

   

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testGetClosestEdge()
   {
      ConvexPolygon2d polygon = createSomeValidPolygon();
      Point2d point = new Point2d(1.314592, 6.0221415); // Useless Riddle: first number is easy, second one multiplied by 1e23 is called?

      assertFalse(polygon.isPointInside(point));

      LineSegment2d closestEdge = polygon.getClosestEdgeCopy(point);

      Point2d closestVertex = polygon.getClosestVertexCopy(point);

      int otherEdgeVertexIndex = 0;
      boolean isClosestVertexPartOfClosestEdge = false;
      for (int i = 0; i < 2; i++)
      {
         Point2d segmentVertex = closestEdge.getEndpoints()[i];
         if (arePointsAtExactlyEqualPosition(closestVertex, segmentVertex))
         {
            isClosestVertexPartOfClosestEdge = true;
            if (i == 0)
               otherEdgeVertexIndex = 1;
         }
      }

      assertTrue(isClosestVertexPartOfClosestEdge);

      int numberOfVertices = polygon.getNumberOfVertices();
      int prevIndex = numberOfVertices - 1;
      int nextIndex = 0;
      for (int i = 0; i < numberOfVertices; i++)
      {
         Point2d currentPoint = polygon.getVertex(i);
         if (arePointsAtExactlyEqualPosition(closestVertex, currentPoint))
         {
            if (i < numberOfVertices - 1)
               nextIndex = i + 1;

            break;
         }

         prevIndex = i;
      }

      Point2d[] neighbourPoints = new Point2d[2];
      neighbourPoints[0] = polygon.getVertex(prevIndex);
      neighbourPoints[1] = polygon.getVertex(nextIndex);
      int neighbourIndex = 1;
      int wrongNeighbourIndex = 0;
      boolean isClosestEdgeVertexThatIsNotClosestVertexNeighbourOfClosestVertex = false;
      for (int i = 0; i < 2; i++)
      {
         Point2d neighbourPoint = neighbourPoints[i];
         Point2d closestEdgeVertexThatIsNotClosest = closestEdge.getEndpoints()[otherEdgeVertexIndex];
         if (arePointsAtExactlyEqualPosition(closestEdgeVertexThatIsNotClosest, neighbourPoint))
         {
            isClosestEdgeVertexThatIsNotClosestVertexNeighbourOfClosestVertex = true;
            neighbourIndex = i;
            if (i == 0)
               wrongNeighbourIndex = 1;
         }

      }

      assertTrue(isClosestEdgeVertexThatIsNotClosestVertexNeighbourOfClosestVertex);

      Line2d segmentLine = new Line2d(neighbourPoints[neighbourIndex], closestVertex);
      Line2d otherLine = new Line2d(neighbourPoints[wrongNeighbourIndex], closestVertex);

      Line2d interiorBiSector = segmentLine.interiorBisector(otherLine);

      boolean isPointBehindLine = interiorBiSector.isPointBehindLine(point);
      boolean isOtherEdgeVertexBehindLine = interiorBiSector.isPointBehindLine(closestEdge.getEndpoints()[otherEdgeVertexIndex]);

      // TODO this may fail, if the point is really close to the "true" biSecotor and the biSector float calc error moves it to the wrong side...
      // TODO edge cases unsolved...
      assertEquals(isPointBehindLine, isOtherEdgeVertexBehindLine);

   }

   private boolean arePointsAtExactlyEqualPosition(Point2d point1, Point2d point2)
   {
      return ((point1.getX() == point2.getX()) && (point1.getY() == point2.getY()));
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testArea()
   {
      Random random = new Random(1888L);

      ReferenceFrame zUpFrame = ReferenceFrame.constructARootFrame("someFrame", true, false, true);
      double xMin1 = 0.0, xMax1 = 1.0, yMin1 = 0.0, yMax1 = 1.0;

      int numberOfPoints = 10000;
      ArrayList<FramePoint2d> points = ConvexPolygon2dTestHelpers.generateRandomCircularFramePoints(random, zUpFrame, xMin1, xMax1, yMin1, yMax1,
            numberOfPoints);

      FrameConvexPolygon2d polygon = new FrameConvexPolygon2d(points);
      double computedArea = polygon.getArea();

      double maximumPossibleArea = Math.PI * 0.5 * 0.5;

      assertTrue(maximumPossibleArea > computedArea);
      assertEquals("computedArea = " + computedArea + ", maximumPossibleArea = " + maximumPossibleArea, maximumPossibleArea, computedArea, 0.02);
   }

   private ConvexPolygon2d createSomeValidPolygon()
   {
      double[][] polygonPoints = new double[][] { { -0.05107802536335158, 0.04155594197133163 }, { -0.05052044462374434, 0.1431544119584275 },
            { 0.12219695435431863, 0.14220652470109518 }, { 0.12219695435431865, -0.041946248489056696 }, { 0.12163937361471142, -0.1435447184761526 },
            { -0.05107802536335154, -0.14259683121882027 } };

      ConvexPolygon2d polygon = new ConvexPolygon2d(polygonPoints);

      return polygon;
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testLargeHullWithIntersections()
   {
      Random random = new Random(1776L);

      ReferenceFrame zUpFrame = ReferenceFrame.constructARootFrame("someFrame", true, false, true);
      double xMin = -100.0, xMax = 100.0, yMin = -100.0, yMax = 100.0;

      ArrayList<FramePoint2d> points = ConvexPolygon2dTestHelpers.generateRandomCircularFramePoints(random, zUpFrame, xMin, xMax, yMin, yMax, 100);

      FrameConvexPolygon2d polygon = new FrameConvexPolygon2d(points);
      ConvexPolygon2dTestHelpers.verifyPointsAreClockwise(polygon);

      // Generate random lines and test the intersections:

      int numberOfTests = 1000;

      ArrayList<FrameLine2d> nonIntersectingLines = new ArrayList<FrameLine2d>();
      ArrayList<FrameLine2d> intersectingLines = new ArrayList<FrameLine2d>();

      ArrayList<FrameLineSegment2d> nonIntersectingLineSegments = new ArrayList<FrameLineSegment2d>();
      ArrayList<FrameLineSegment2d> intersectingLineSegments = new ArrayList<FrameLineSegment2d>();

      ArrayList<FramePoint2d> intersectingPoints = new ArrayList<FramePoint2d>();

      for (int i = 0; i < numberOfTests; i++)
      {
         FrameLine2d testLine = FrameLine2d.generateRandomFrameLine2d(random, zUpFrame, 2.0 * xMin, 2.0 * xMax, 2.0 * yMin, 2.0 * yMax);
         FramePoint2d[] intersectionsWithLine = polygon.intersectionWith(testLine);

         if (intersectionsWithLine == null)
            nonIntersectingLines.add(testLine);
         else
         {
            intersectingLines.add(testLine);

            intersectingPoints.add(intersectionsWithLine[0]);
            if (intersectionsWithLine.length > 1)
               intersectingPoints.add(intersectionsWithLine[1]);
         }

         FrameLineSegment2d testLineSegment = FrameLineSegment2d.generateRandomFrameLineSegment2d(random, zUpFrame, 2.0 * xMin, 2.0 * xMax, 2.0 * yMin,
               2.0 * yMax);

         FramePoint2d[] intersectionsWithLineSegment = polygon.intersectionWith(testLineSegment);

         if (intersectionsWithLineSegment == null)
            nonIntersectingLineSegments.add(testLineSegment);
         else
         {
            intersectingLineSegments.add(testLineSegment);
            intersectingPoints.add(intersectionsWithLineSegment[0]);
            if (intersectionsWithLineSegment.length > 1)
               intersectingPoints.add(intersectionsWithLineSegment[1]);
         }
      }

      ConvexPolygon2dTestHelpers.verifyLinesIntersectPolygon(polygon, intersectingLines);
      ConvexPolygon2dTestHelpers.verifyLineSegmentsIntersectPolygon(polygon, intersectingLineSegments);
      ConvexPolygon2dTestHelpers.verifyLinesDoNotIntersectPolygon(polygon, nonIntersectingLines);
      ConvexPolygon2dTestHelpers.verifyLineSegmentsDoNotIntersectPolygon(polygon, nonIntersectingLineSegments);

      if (PLOT_RESULTS)
      {
         FrameGeometryTestFrame testFrame = new FrameGeometryTestFrame(xMin, xMax, yMin, yMax);

         FrameGeometry2dPlotter plotter = testFrame.getFrameGeometry2dPlotter();
         plotter.setPolygonToCheckInside(polygon);

         plotter.addFrameLines2d(intersectingLines, Color.green);
         plotter.addFrameLines2d(nonIntersectingLines, Color.red);

         plotter.addFrameLineSegments2d(intersectingLineSegments, Color.green);
         plotter.addFrameLineSegments2d(nonIntersectingLineSegments, Color.red);

         testFrame.addTestPoints(intersectingPoints);

         waitForButtonOrPause(testFrame);
      }

   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testGetDistanceInside()
   {
      double[][] vertices = new double[][]{{0.0, 0.0}, {0.0, 1.0}, {1.0, 1.0}, {1.0, 0.0}};
      
      ConvexPolygon2d convexPolygon = new ConvexPolygon2d(vertices);
      
      Point2d point = new Point2d(0.0, 0.0);
      assertEquals(0.0, convexPolygon.getDistanceInside(point), 1e-7);
      
      point = new Point2d(0.5, 0.8);
      assertEquals(0.2, convexPolygon.getDistanceInside(point), 1e-7);
      
      point = new Point2d(0.5, 1.2);
      assertEquals(-0.2, convexPolygon.getDistanceInside(point), 1e-7);
      
      point = new Point2d(0.5, 0.5);
      assertEquals(0.5, convexPolygon.getDistanceInside(point), 1e-7);
      
      // Note: This 1.0 isn't the true distance outside. It's the distance to 
      // a projected edge. The distance is only exact when the point is inside.
      point = new Point2d(2.0, 2.0);
      assertEquals(-1.0, convexPolygon.getDistanceInside(point), 1e-7);

      vertices = new double[][]{{0.0, 1.0}, {1.0, 1.0}, {1.0, 0.0}};
      
      convexPolygon = new ConvexPolygon2d(vertices);
      
      point = new Point2d(0.0, 0.0);
      assertEquals(-0.70710678118, convexPolygon.getDistanceInside(point), 1e-7);
      
      point = new Point2d(0.5, 0.5);
      assertEquals(0.0, convexPolygon.getDistanceInside(point), 1e-7);
      
      point = new Point2d(0.6, 0.6);
      assertEquals(0.141421356, convexPolygon.getDistanceInside(point), 1e-7);
      
      vertices = new double[][]{{0.0, 1.0}, {1.0, 0.0}};
      
      convexPolygon = new ConvexPolygon2d(vertices);
      
      point = new Point2d(0.0, 0.0);
      assertEquals(-0.70710678118, convexPolygon.getDistanceInside(point), 1e-7);
      
      point = new Point2d(0.5, 0.5);
      assertEquals(0.0, convexPolygon.getDistanceInside(point), 1e-7);
      
      point = new Point2d(0.6, 0.6);
      assertEquals(-0.141421356, convexPolygon.getDistanceInside(point), 1e-7);
      
      vertices = new double[][]{{0.0, 1.0}};
      
      convexPolygon = new ConvexPolygon2d(vertices);
      
      point = new Point2d(0.0, 0.0);
      assertEquals(-1.0, convexPolygon.getDistanceInside(point), 1e-7);
      
      point = new Point2d(0.5, 0.5);
      assertEquals(-0.70710678118, convexPolygon.getDistanceInside(point), 1e-7);
      
      point = new Point2d(0.6, 0.6);
      assertEquals(-0.721110255, convexPolygon.getDistanceInside(point), 1e-7);
   }

   
   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testOrthogonalProjectionPointConvexPolygon2d()
   {
      ArrayList<Point2d> pointList = new ArrayList<Point2d>();
      ConvexPolygon2d convexPolygon;
      Point2d testPoint = new Point2d();

      for (int i = 4; i < 10; i++) // polygon sizes
      {
         pointList.clear();

         for (int j = 0; j < i; j++) // points from which the polygon is constructed
         {
            pointList.add(new Point2d(random.nextDouble(), random.nextDouble()));
         }

         convexPolygon = new ConvexPolygon2d(pointList);

         for (int k = 0; k < 100; k++) // testPoints for isPointInside()
         {
            testPoint.set(random.nextDouble(), random.nextDouble());

            if (VERBOSE)
            {
               if ((i == 9) && (k == 69))
               {
                  System.out.println("convexPolygon = " + convexPolygon);
                  System.out.println("testPoint = " + testPoint);
               }
            }

            Point2d projectedPoint = convexPolygon.orthogonalProjectionCopy(testPoint);

            assertTrue("Projected point was not inside the polygon for point\n" + projectedPoint + "\nand convex polygon \n" + convexPolygon,
                  convexPolygon.isPointInside(projectedPoint));
         }
      }
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testAllMethodsForPolygonWithOnePoint()
   {
      int numberOfTrials = 100;
      double epsilon = 1e-7;
      Random random = new Random(756483920L);

      for (int i = 0; i < numberOfTrials; i++)
      {
         ArrayList<Point2d> points = new ArrayList<Point2d>();
         Point2d pointThatDefinesThePolygon = new Point2d(random.nextDouble(), random.nextDouble());
         points.add(pointThatDefinesThePolygon);
         ConvexPolygon2d polygonWithOnePoint = new ConvexPolygon2d(points);
         points.clear();
         Point2d pointThatDefinesAnotherPolygon = new Point2d(random.nextDouble(), random.nextDouble());
         points.add(pointThatDefinesAnotherPolygon);
         ConvexPolygon2d anotherPolygonWithOnePoint = new ConvexPolygon2d(points);
         points.clear();
         points.add(new Point2d(random.nextDouble(), random.nextDouble()));
         points.add(new Point2d(random.nextDouble(), random.nextDouble()));
         points.add(new Point2d(random.nextDouble(), random.nextDouble()));
         ConvexPolygon2d sparePolygon = new ConvexPolygon2d(points);
         Point2d arbitraryPoint0 = new Point2d(random.nextDouble(), random.nextDouble());
         Point2d arbitraryPoint1 = new Point2d(random.nextDouble(), random.nextDouble());
         Line2d arbitraryLine = new Line2d(arbitraryPoint0, arbitraryPoint1);
         LineSegment2d arbitraryLineSegment = new LineSegment2d(arbitraryPoint0, arbitraryPoint1);

         assertFalse(polygonWithOnePoint.areAllPointsInside(new Point2d[] {arbitraryPoint0}));
         assertEquals(pointThatDefinesThePolygon.distance(arbitraryPoint0), polygonWithOnePoint.distanceToClosestVertex(arbitraryPoint0), epsilon);
         assertEquals(1, polygonWithOnePoint.distanceToEachVertex(arbitraryPoint0).length);
         assertEquals(pointThatDefinesThePolygon.distance(arbitraryPoint0), polygonWithOnePoint.distanceToEachVertex(arbitraryPoint0)[0], epsilon);
         assertTrue(polygonWithOnePoint.getAllVisibleVerticesFromOutsideLeftToRightCopy(arbitraryPoint0).get(0).equals(pointThatDefinesThePolygon));
         assertEquals(0.0, polygonWithOnePoint.getArea(), epsilon);
         assertTrue(polygonWithOnePoint.getAroundTheCornerEdgesCopy(arbitraryPoint0) == null);
         assertTrue(polygonWithOnePoint.getBoundingBoxCopy().getMaxPoint().equals(pointThatDefinesThePolygon));
         assertTrue(polygonWithOnePoint.getBoundingBoxCopy().getMinPoint().equals(pointThatDefinesThePolygon));
         assertTrue(polygonWithOnePoint.getCentroid().equals(pointThatDefinesThePolygon));
         assertEquals(1, polygonWithOnePoint.getNumberOfVertices());
         assertTrue(polygonWithOnePoint.getVertex(0).equals(pointThatDefinesThePolygon));
         assertTrue(polygonWithOnePoint.getClosestEdgeCopy(arbitraryPoint0) == null);
         assertTrue(polygonWithOnePoint.getClosestEdgeVertexIndicesInClockwiseOrderedList(arbitraryPoint0) == null);
         assertTrue(polygonWithOnePoint.getClosestVertexCopy(arbitraryLine).equals(pointThatDefinesThePolygon));
         assertTrue(polygonWithOnePoint.getClosestVertexCopy(arbitraryPoint0).equals(pointThatDefinesThePolygon));
         assertEquals(1, polygonWithOnePoint.getNumberOfVertices());
         assertTrue(polygonWithOnePoint.getVertexCCW(0).equals(pointThatDefinesThePolygon));
         assertTrue(polygonWithOnePoint.getIntersectingEdges(arbitraryLine) == null);
         assertEquals(1, polygonWithOnePoint.getLineOfSightVerticesCopy(arbitraryPoint0).length);
         assertTrue(polygonWithOnePoint.getLineOfSightVerticesCopy(arbitraryPoint0)[0].equals(pointThatDefinesThePolygon));
         assertEquals(1, polygonWithOnePoint.getLinesOfSight(arbitraryPoint0).length);
         assertTrue(polygonWithOnePoint.getLinesOfSight(arbitraryPoint0)[0].equals(new Line2d(arbitraryPoint0, pointThatDefinesThePolygon)));
         assertTrue(polygonWithOnePoint.getCentroid().equals(pointThatDefinesThePolygon));
         assertTrue(polygonWithOnePoint.getNearestEdges(arbitraryPoint0) == null);
         assertEquals(1, polygonWithOnePoint.getNumberOfVertices());
         assertTrue(polygonWithOnePoint.getOutSideFacingOrthoNormalVectorsCopy() == null);
         assertEquals(1, polygonWithOnePoint.getNumberOfVertices());
         assertTrue(polygonWithOnePoint.getVertex(0).equals(pointThatDefinesThePolygon));
         assertTrue(polygonWithOnePoint.intersectionWith(sparePolygon) == null);
         assertTrue(polygonWithOnePoint.intersectionWith(arbitraryLine) == null);
         assertFalse(polygonWithOnePoint.isPointInside(arbitraryPoint0));
         assertFalse(polygonWithOnePoint.isPolygonInside(sparePolygon));
         assertTrue(polygonWithOnePoint.getMaxXMaxYPointCopy().equals(pointThatDefinesThePolygon));
         assertTrue(polygonWithOnePoint.getMaxXMinYPointCopy().equals(pointThatDefinesThePolygon));
         assertTrue(polygonWithOnePoint.getMinXMaxYPointCopy().equals(pointThatDefinesThePolygon));
         assertTrue(polygonWithOnePoint.getMinXMinYPointCopy().equals(pointThatDefinesThePolygon));
         assertTrue(polygonWithOnePoint.orthogonalProjectionCopy(arbitraryPoint0).equals(pointThatDefinesThePolygon));
         assertEquals(0.0, polygonWithOnePoint.perimeter(), epsilon);
         assertTrue(polygonWithOnePoint.pointIsOnPerimeter(pointThatDefinesThePolygon));
         assertFalse(polygonWithOnePoint.pointIsOnPerimeter(arbitraryPoint0));
         assertTrue(polygonWithOnePoint.pointOnPerimeterGivenParameter(random.nextDouble()).equals(pointThatDefinesThePolygon));

         double pullPercent = random.nextDouble();
         Point2d polygonResult = polygonWithOnePoint.pullTowardsCentroidCopy(arbitraryPoint0, pullPercent);
         Point2d pointResult = new Point2d(arbitraryPoint0);
         pointResult.interpolate(pointThatDefinesThePolygon, pullPercent);
         assertTrue(polygonResult.distance(pointResult) < epsilon);

         ConvexPolygon2d polygonTranslation = polygonWithOnePoint.translateCopy(arbitraryPoint0);
         assertEquals(1, polygonTranslation.getNumberOfVertices());
         Point2d pointTranslation = new Point2d(pointThatDefinesThePolygon);
         pointTranslation.add(arbitraryPoint0);
         assertEquals(polygonTranslation.getVertex(0), pointTranslation);

         ConvexPolygon2d combinedPolygons = new ConvexPolygon2d(polygonWithOnePoint, anotherPolygonWithOnePoint);
         assertEquals(2, combinedPolygons.getNumberOfVertices());
         Point2d point0 = combinedPolygons.getVertex(0);
         Point2d point1 = combinedPolygons.getVertex(1);
         assertEqualsInEitherOrder(pointThatDefinesThePolygon, pointThatDefinesAnotherPolygon, point0, point1);

         ConvexPolygon2dAndConnectingEdges combinedDisjointPolygons = ConvexPolygonTools.combineDisjointPolygons(polygonWithOnePoint, anotherPolygonWithOnePoint);
         assertEquals(2, combinedDisjointPolygons.getConvexPolygon2d().getNumberOfVertices());
         point0 = combinedDisjointPolygons.getConvexPolygon2d().getVertex(0);
         point1 = combinedDisjointPolygons.getConvexPolygon2d().getVertex(1);
         assertEqualsInEitherOrder(pointThatDefinesThePolygon, pointThatDefinesAnotherPolygon, point0, point1);

         assertTrue(ConvexPolygonTools.computeIntersectionOfPolygons(polygonWithOnePoint, anotherPolygonWithOnePoint, new ConvexPolygon2d()) == false);
         ConvexPolygon2d intersection = new ConvexPolygon2d();
         ConvexPolygonTools.computeIntersectionOfPolygons(polygonWithOnePoint, polygonWithOnePoint, intersection);
         assertEquals(1, intersection.getNumberOfVertices());
         ConvexPolygonTools.computeIntersectionOfPolygons(polygonWithOnePoint, polygonWithOnePoint, intersection);
         assertTrue(intersection.getVertex(0).equals(pointThatDefinesThePolygon));
         assertTrue(ConvexPolygonTools.intersection(arbitraryLineSegment, polygonWithOnePoint) == null);
         assertTrue(ConvexPolygonTools.intersection(new LineSegment2d(pointThatDefinesThePolygon, arbitraryPoint0),
               polygonWithOnePoint)[0].equals(pointThatDefinesThePolygon));
         
         ConvexPolygonShrinker shrinker = new ConvexPolygonShrinker();
         ConvexPolygon2d shrunkenOnePointPolygon = new ConvexPolygon2d();
         
         shrinker.shrinkConstantDistanceInto(polygonWithOnePoint, random.nextDouble(), shrunkenOnePointPolygon);
         
         assertTrue(shrunkenOnePointPolygon.epsilonEquals(polygonWithOnePoint, 1e-7));
         
         assertEquals(1, ConvexPolygonTools.shrinkInto(sparePolygon, arbitraryPoint0, polygonWithOnePoint).getNumberOfVertices());
         assertTrue(ConvexPolygonTools.shrinkInto(sparePolygon, arbitraryPoint0,
               polygonWithOnePoint).getVertex(0).equals(pointThatDefinesThePolygon));
      }
   }

   @DeployableTestMethod(estimatedDuration = 0.1)
   @Test(timeout = 30000)
   public void testAllMethodsForPolygonWithTwoPoints()
   {
      int numberOfTrials = 100;
      double epsilon = 1e-7;
      Random random = new Random(756483920L);

      for (int i = 0; i < numberOfTrials; i++)
      {
         ArrayList<Point2d> points = new ArrayList<Point2d>();
         Point2d pointThatDefinesThePolygon0 = new Point2d(random.nextDouble(), random.nextDouble());
         Point2d pointThatDefinesThePolygon1 = new Point2d(random.nextDouble(), random.nextDouble());
         LineSegment2d lineSegmentThatDefinesThePolygon = new LineSegment2d(pointThatDefinesThePolygon0, pointThatDefinesThePolygon1);
         points.add(pointThatDefinesThePolygon0);
         points.add(pointThatDefinesThePolygon1);
         ConvexPolygon2d polygonWithTwoPoints = new ConvexPolygon2d(points);
         points.clear();
         Point2d pointThatDefinesAnotherPolygon = new Point2d(random.nextDouble(), random.nextDouble());
         points.add(pointThatDefinesAnotherPolygon);
         ConvexPolygon2d polygonWithOnePointx = new ConvexPolygon2d(points);
         points.clear();
         points.add(new Point2d(random.nextDouble(), random.nextDouble()));
         points.add(new Point2d(random.nextDouble(), random.nextDouble()));
         points.add(new Point2d(random.nextDouble(), random.nextDouble()));
         ConvexPolygon2d sparePolygon = new ConvexPolygon2d(points);
         Point2d arbitraryPoint0 = new Point2d(random.nextDouble(), random.nextDouble());
         Point2d arbitraryPoint1 = new Point2d(random.nextDouble(), random.nextDouble());
         Line2d arbitraryLine = new Line2d(arbitraryPoint0, arbitraryPoint1);
         LineSegment2d arbitraryLineSegment = new LineSegment2d(arbitraryPoint0, arbitraryPoint1);

         // one line tests
         assertFalse(polygonWithTwoPoints.areAllPointsInside(new Point2d[] { arbitraryPoint0 }));
         assertEquals(Math.min(pointThatDefinesThePolygon0.distance(arbitraryPoint0), pointThatDefinesThePolygon1.distance(arbitraryPoint0)),
               polygonWithTwoPoints.distanceToClosestVertex(arbitraryPoint0), epsilon);
         assertEquals(2, polygonWithTwoPoints.distanceToEachVertex(arbitraryPoint0).length);
         assertEqualsInEitherOrder(pointThatDefinesThePolygon0.distance(arbitraryPoint0), pointThatDefinesThePolygon1.distance(arbitraryPoint0),
               polygonWithTwoPoints.distanceToEachVertex(arbitraryPoint0)[0], polygonWithTwoPoints.distanceToEachVertex(arbitraryPoint0)[1]);
         assertEqualsInEitherOrder(pointThatDefinesThePolygon0, pointThatDefinesThePolygon1, polygonWithTwoPoints
               .getAllVisibleVerticesFromOutsideLeftToRightCopy(arbitraryPoint0).get(0),
               polygonWithTwoPoints.getAllVisibleVerticesFromOutsideLeftToRightCopy(arbitraryPoint0).get(1));
         assertEquals(0.0, polygonWithTwoPoints.getArea(), epsilon);
         LineSegment2d aroundTheCornerEdge0 = polygonWithTwoPoints.getAroundTheCornerEdgesCopy(arbitraryPoint0)[0];
         LineSegment2d aroundTheCornerEdge1 = polygonWithTwoPoints.getAroundTheCornerEdgesCopy(arbitraryPoint0)[1];
         assertEqualsInEitherOrder(pointThatDefinesThePolygon0, pointThatDefinesThePolygon1, aroundTheCornerEdge0.getFirstEndPointCopy(),
               aroundTheCornerEdge0.getSecondEndPointCopy());
         assertEqualsInEitherOrder(pointThatDefinesThePolygon0, pointThatDefinesThePolygon1, aroundTheCornerEdge1.getFirstEndPointCopy(),
               aroundTheCornerEdge1.getSecondEndPointCopy());
         Point2d minPoint = new Point2d(Math.min(pointThatDefinesThePolygon0.getX(), pointThatDefinesThePolygon1.getX()), Math.min(
               pointThatDefinesThePolygon0.getY(), pointThatDefinesThePolygon1.getY()));
         Point2d maxPoint = new Point2d(Math.max(pointThatDefinesThePolygon0.getX(), pointThatDefinesThePolygon1.getX()), Math.max(
               pointThatDefinesThePolygon0.getY(), pointThatDefinesThePolygon1.getY()));
         assertTrue(polygonWithTwoPoints.getBoundingBoxCopy().getMinPoint().equals(minPoint));
         assertTrue(polygonWithTwoPoints.getBoundingBoxCopy().getMaxPoint().equals(maxPoint));
         assertTrue(polygonWithTwoPoints.getCentroid().equals(lineSegmentThatDefinesThePolygon.midpoint()));
         assertEquals(2, polygonWithTwoPoints.getNumberOfVertices());
         assertEqualsInEitherOrder(pointThatDefinesThePolygon0, pointThatDefinesThePolygon1, polygonWithTwoPoints.getVertex(0),
               polygonWithTwoPoints.getVertex(1));
         assertFalse(polygonWithTwoPoints.isPointInside(arbitraryPoint0));
         assertFalse(polygonWithTwoPoints.isPolygonInside(sparePolygon));
         assertEquals(2, polygonWithTwoPoints.getNumberOfVertices());
         assertTrue(polygonWithTwoPoints.getCentroid().x == 0.5 * (pointThatDefinesThePolygon0.x + pointThatDefinesThePolygon1.x));
         assertTrue(polygonWithTwoPoints.getCentroid().y == 0.5 * (pointThatDefinesThePolygon0.y + pointThatDefinesThePolygon1.y));
         assertEquals(2, polygonWithTwoPoints.getNumberOfVertices());
         assertEquals(2.0 * pointThatDefinesThePolygon0.distance(pointThatDefinesThePolygon1), polygonWithTwoPoints.perimeter(), epsilon);

         // getClosestEdge
         Point2d[] closestEdgeEndpoints = polygonWithTwoPoints.getClosestEdgeCopy(arbitraryPoint0).getEndpoints();
         assertEqualsInEitherOrder(closestEdgeEndpoints[0], closestEdgeEndpoints[1], pointThatDefinesThePolygon0, pointThatDefinesThePolygon1);

         // getClosestEdgeVertexIndicesInClockwiseOrderedList
         int[] closestEdgesClockwise = polygonWithTwoPoints.getClosestEdgeVertexIndicesInClockwiseOrderedList(arbitraryPoint0);
         assertEqualsInEitherOrder(closestEdgesClockwise[0], closestEdgesClockwise[1], 0, 1);

         // getCounterClockwiseOrderedListOfPointsCopy
         assertEqualsInEitherOrder(polygonWithTwoPoints.getVertexCCW(0), polygonWithTwoPoints.getVertexCCW(1), pointThatDefinesThePolygon0,
               pointThatDefinesThePolygon1);

         // getLinesOfSight
         Line2d[] linesOfSight = polygonWithTwoPoints.getLinesOfSight(arbitraryPoint0);
         assertEquals(2, linesOfSight.length);
         assertEqualsInEitherOrder(linesOfSight[0], linesOfSight[1], new Line2d(arbitraryPoint0, pointThatDefinesThePolygon0), new Line2d(arbitraryPoint0,
               pointThatDefinesThePolygon1));

         // getLineOfSightVertices
         assertEquals(2, polygonWithTwoPoints.getLineOfSightVerticesCopy(arbitraryPoint0).length);
         Point2d[] lineOfSightPoints = polygonWithTwoPoints.getLineOfSightVerticesCopy(arbitraryPoint0);
         assertEqualsInEitherOrder(lineOfSightPoints[0], lineOfSightPoints[1], pointThatDefinesThePolygon0, pointThatDefinesThePolygon1);

         // getNearestEdges
         LineSegment2d[] nearestEdges = polygonWithTwoPoints.getNearestEdges(arbitraryPoint0);
         assertTrue(nearestEdges.length == 1);
         assertEqualsInEitherOrder(nearestEdges[0].getEndpoints()[0], nearestEdges[0].getEndpoints()[1], pointThatDefinesThePolygon0,
               pointThatDefinesThePolygon1);

         // orthoganolProjectionCopy
         Point2d expectedProjection = lineSegmentThatDefinesThePolygon.orthogonalProjectionCopy(arbitraryPoint0);
         Point2d actualProjection = polygonWithTwoPoints.orthogonalProjectionCopy(arbitraryPoint0);
         assertTrue(expectedProjection.epsilonEquals(actualProjection, epsilon));

         // getClosestVertexCopy
         Point2d closestVertexToLine = polygonWithTwoPoints.getClosestVertexCopy(arbitraryLine);
         if (arbitraryLine.distance(pointThatDefinesThePolygon0) < arbitraryLine.distance(pointThatDefinesThePolygon1))
            assertEquals(closestVertexToLine, pointThatDefinesThePolygon0);
         else
            assertEquals(closestVertexToLine, pointThatDefinesThePolygon1);

         Point2d closestVertexToPoint = polygonWithTwoPoints.getClosestVertexCopy(arbitraryPoint0);
         if (arbitraryPoint0.distance(pointThatDefinesThePolygon0) < arbitraryPoint0.distance(pointThatDefinesThePolygon1))
            assertEquals(closestVertexToPoint, pointThatDefinesThePolygon0);
         else
            assertEquals(closestVertexToPoint, pointThatDefinesThePolygon1);

         // getOutSideFacingOrthoNormalVectors
         List<Vector2d> actualOrthoganolVectors = polygonWithTwoPoints.getOutSideFacingOrthoNormalVectorsCopy();
         assertTrue(actualOrthoganolVectors.size() == 2);
         Vector2d oneExpectedOrthoganol = new Vector2d(pointThatDefinesThePolygon1.y - pointThatDefinesThePolygon0.y, pointThatDefinesThePolygon0.x
               - pointThatDefinesThePolygon1.x);
         oneExpectedOrthoganol.normalize();
         assertEqualsInEitherOrder(oneExpectedOrthoganol.x, -oneExpectedOrthoganol.x, actualOrthoganolVectors.get(0).x, actualOrthoganolVectors.get(1).x);
         assertEqualsInEitherOrder(oneExpectedOrthoganol.y, -oneExpectedOrthoganol.y, actualOrthoganolVectors.get(0).y, actualOrthoganolVectors.get(1).y);

         // pullTowardsCentroidCopy
         double pullPercent = random.nextDouble();
         Point2d polygonResult = polygonWithTwoPoints.pullTowardsCentroidCopy(arbitraryPoint0, pullPercent);
         Point2d pointResult = new Point2d(arbitraryPoint0);
         pointResult.interpolate(polygonWithTwoPoints.getCentroid(), pullPercent);
         assertTrue(polygonResult.distance(pointResult) < epsilon);

         // getIntersectingEdges
         LineSegment2d[] intersectingEdges = polygonWithTwoPoints.getIntersectingEdges(arbitraryLine);
         boolean isLineAbovePoint0 = ((pointThatDefinesThePolygon0.x - arbitraryLine.getPoint().x) * arbitraryLine.getSlope() + arbitraryLine.getPoint().y) >= pointThatDefinesThePolygon0.y;
         boolean isLineAbovePoint1 = ((pointThatDefinesThePolygon1.x - arbitraryLine.getPoint().x) * arbitraryLine.getSlope() + arbitraryLine.getPoint().y) >= pointThatDefinesThePolygon1.y;
         boolean lineCrossesThroughPolygon = isLineAbovePoint0 ^ isLineAbovePoint1;

         if (!lineCrossesThroughPolygon)
         {
            assertTrue(intersectingEdges == null);
         }
         else
         {
            for (int j : new int[] { 0, 1 })
            {
               Point2d[] endPoints = intersectingEdges[j].getEndpoints();
               assertEqualsInEitherOrder(endPoints[0], endPoints[1], pointThatDefinesThePolygon0, pointThatDefinesThePolygon1);
            }
         }

         // getStartingFromLeftMostClockwiseOrderedListOfPointsCopy
         assertEquals(2, polygonWithTwoPoints.getNumberOfVertices());
         Point2d leftPoint, rightPoint;
         if (pointThatDefinesThePolygon0.x <= pointThatDefinesThePolygon1.x)
         {
            leftPoint = pointThatDefinesThePolygon0;
            rightPoint = pointThatDefinesThePolygon1;
         }
         else
         {
            leftPoint = pointThatDefinesThePolygon1;
            rightPoint = pointThatDefinesThePolygon0;
         }

         assertTrue((leftPoint.x == polygonWithTwoPoints.getVertex(0).x) && (leftPoint.y == polygonWithTwoPoints.getVertex(0).y));
         assertTrue((rightPoint.x == polygonWithTwoPoints.getVertex(1).x) && (rightPoint.y == polygonWithTwoPoints.getVertex(1).y));

         // maxXMaxYPointCopy, maxXMinYPointCopy, minXMaxYPointCopy, minXMinYPointCopy 
         Point2d maxXPoint, minXPoint;
         if (pointThatDefinesThePolygon0.x > pointThatDefinesThePolygon1.x)
         {
            maxXPoint = pointThatDefinesThePolygon0;
            minXPoint = pointThatDefinesThePolygon1;
         }
         else
         {
            maxXPoint = pointThatDefinesThePolygon1;
            minXPoint = pointThatDefinesThePolygon0;
         }

         assertTrue(polygonWithTwoPoints.getMaxXMaxYPointCopy().equals(maxXPoint));
         assertTrue(polygonWithTwoPoints.getMaxXMinYPointCopy().equals(maxXPoint));
         boolean test = polygonWithTwoPoints.getMinXMaxYPointCopy().equals(minXPoint);
         if (!test)
            System.out.println();
         assertTrue(test);
         assertTrue(polygonWithTwoPoints.getMinXMinYPointCopy().equals(minXPoint));

         // intersectionWith
         Point2d[] expectedIntersectionWithSparePolygon = sparePolygon.intersectionWith(new LineSegment2d(pointThatDefinesThePolygon0,
               pointThatDefinesThePolygon1));
         ConvexPolygon2d actualIntersectionWithSparePolygon = sparePolygon.intersectionWith(polygonWithTwoPoints);

         if (expectedIntersectionWithSparePolygon == null)
         {
            assertTrue(actualIntersectionWithSparePolygon == null);
         }
         else if (expectedIntersectionWithSparePolygon.length == 1)
         {
            assertTrue(actualIntersectionWithSparePolygon.getNumberOfVertices() == 1);
            assertTrue(expectedIntersectionWithSparePolygon[0].equals(actualIntersectionWithSparePolygon.getVertex(0)));
         }
         else if (expectedIntersectionWithSparePolygon.length == 2)
         {
            assertTrue(actualIntersectionWithSparePolygon.getNumberOfVertices() == 2);
            assertEqualsInEitherOrder(expectedIntersectionWithSparePolygon[0], expectedIntersectionWithSparePolygon[1],
                  actualIntersectionWithSparePolygon.getVertex(0), actualIntersectionWithSparePolygon.getVertex(1));
         }
         else
         {
            fail();
         }

         // pointIsOnPerimeter
         double randomFraction = random.nextDouble();
         Point2d scaledPoint0 = new Point2d(pointThatDefinesThePolygon0);
         Point2d scaledPoint1 = new Point2d(pointThatDefinesThePolygon1);
         scaledPoint0.scale(randomFraction);
         scaledPoint1.scale(1 - randomFraction);
         Point2d randomLinearCombination = new Point2d();
         randomLinearCombination.add(scaledPoint0, scaledPoint1);
         assertTrue(polygonWithTwoPoints.pointIsOnPerimeter(randomLinearCombination));

         // pointOnPerimeterGivenParameter
         Vector2d slightlyOffPerimeter = polygonWithTwoPoints.getOutSideFacingOrthoNormalVectorsCopy().get(0);
         slightlyOffPerimeter.scale(0.01);
         slightlyOffPerimeter.add(randomLinearCombination);
         assertFalse(polygonWithTwoPoints.pointIsOnPerimeter(new Point2d(slightlyOffPerimeter.x, slightlyOffPerimeter.y)));
         double randomParameter = random.nextDouble();
         double desiredFractionFrom0to1 = 2.0 * ((randomParameter > 0.5) ? 1.0 - randomParameter : randomParameter);
         scaledPoint0 = new Point2d(polygonWithTwoPoints.getVertex(0));
         scaledPoint1 = new Point2d(polygonWithTwoPoints.getVertex(1));
         scaledPoint0.scale(1.0 - desiredFractionFrom0to1);
         scaledPoint1.scale(desiredFractionFrom0to1);
         Point2d expectedPointFromParameter = new Point2d();
         expectedPointFromParameter.add(scaledPoint0, scaledPoint1);
         Point2d actualPointFromParameter = polygonWithTwoPoints.pointOnPerimeterGivenParameter(randomParameter);
         assertTrue(expectedPointFromParameter.epsilonEquals(actualPointFromParameter, epsilon));

         // STATIC METHODS

         // translateCopy
         ConvexPolygon2d polygonTranslation = polygonWithTwoPoints.translateCopy(arbitraryPoint0);
         assertEquals(2, polygonTranslation.getNumberOfVertices());
         Point2d pointTranslation0 = new Point2d(pointThatDefinesThePolygon0);
         Point2d pointTranslation1 = new Point2d(pointThatDefinesThePolygon1);
         pointTranslation0.add(arbitraryPoint0);
         pointTranslation1.add(arbitraryPoint0);
         assertEqualsInEitherOrder(polygonTranslation.getVertex(0), polygonTranslation.getVertex(1), pointTranslation0, pointTranslation1);

         // combinePolygons
         ConvexPolygon2d combinedPolygons = new ConvexPolygon2d(polygonWithTwoPoints, polygonWithOnePointx);
         assertEquals(3, combinedPolygons.getNumberOfVertices());
         Point2d point0 = combinedPolygons.getVertex(0);
         Point2d point1 = combinedPolygons.getVertex(1);
         Point2d point2 = combinedPolygons.getVertex(2);
         assertEqualsInAnyOrder(point0, point1, point2, pointThatDefinesThePolygon0, pointThatDefinesThePolygon1, pointThatDefinesAnotherPolygon);

         // combineDisjointPolygons
         ConvexPolygon2dAndConnectingEdges combinedDisjointPolygons = ConvexPolygonTools.combineDisjointPolygons(polygonWithTwoPoints, polygonWithOnePointx);
         assertEquals(3, combinedDisjointPolygons.getConvexPolygon2d().getNumberOfVertices());
         point0 = combinedDisjointPolygons.getConvexPolygon2d().getVertex(0);
         point1 = combinedDisjointPolygons.getConvexPolygon2d().getVertex(1);
         point2 = combinedDisjointPolygons.getConvexPolygon2d().getVertex(2);
         assertEqualsInAnyOrder(point0, point1, point2, pointThatDefinesThePolygon0, pointThatDefinesThePolygon1, pointThatDefinesAnotherPolygon);

         // computeIntersectionOfPolygons
         ConvexPolygon2d polygonIntersection = new ConvexPolygon2d();
         boolean success = ConvexPolygonTools.computeIntersectionOfPolygons(polygonWithTwoPoints, sparePolygon, polygonIntersection);
         if (!success)
            assertTrue(sparePolygon.intersectionWith(lineSegmentThatDefinesThePolygon) == null);
         else if (polygonIntersection.getNumberOfVertices() == 1)
            assertTrue(sparePolygon.intersectionWith(lineSegmentThatDefinesThePolygon)[0].equals(polygonIntersection.getVertex(0)));
         else if (polygonIntersection.getNumberOfVertices() == 2)
            assertEqualsInEitherOrder(sparePolygon.intersectionWith(lineSegmentThatDefinesThePolygon)[0],
                  sparePolygon.intersectionWith(lineSegmentThatDefinesThePolygon)[1], polygonIntersection.getVertex(0), polygonIntersection.getVertex(1));
         else
            fail();

         // intersection
         Point2d[] intersection = ConvexPolygonTools.intersection(arbitraryLineSegment, polygonWithTwoPoints);
         if (intersection == null)
            assertTrue(arbitraryLineSegment.intersectionWith(lineSegmentThatDefinesThePolygon) == null);
         else if (intersection.length == 1)
            assertTrue(intersection[0].distance(arbitraryLineSegment.intersectionWith(lineSegmentThatDefinesThePolygon)) < epsilon);
         else
            fail();

         // shrinkConstantDistanceInto
         double shrinkDistance = random.nextDouble() * lineSegmentThatDefinesThePolygon.length() / 2.0;
         
         ConvexPolygonShrinker shrinker = new ConvexPolygonShrinker();

         ConvexPolygon2d shrunkenPolygon = new ConvexPolygon2d();
         
         shrinker.shrinkConstantDistanceInto(polygonWithTwoPoints, shrinkDistance, shrunkenPolygon);
         
         assertEquals(shrunkenPolygon.perimeter(), polygonWithTwoPoints.perimeter() - 4.0
               * shrinkDistance, epsilon);
         shrinkDistance = lineSegmentThatDefinesThePolygon.length() / 2.0 + random.nextDouble();
         
         shrinker.shrinkConstantDistanceInto(polygonWithTwoPoints, shrinkDistance, shrunkenPolygon);
         
         assertTrue(shrunkenPolygon.getNumberOfVertices() == 1);
         ConvexPolygon2d shrinkInto = ConvexPolygonTools.shrinkInto(sparePolygon, arbitraryPoint0, polygonWithTwoPoints);
         assertEquals(2, shrinkInto.getNumberOfVertices());
         shrinkInto = ConvexPolygonTools.shrinkInto(sparePolygon, arbitraryPoint0, polygonWithTwoPoints);
         assertEqualsInEitherOrder(shrinkInto.getVertex(0), shrinkInto.getVertex(1), pointThatDefinesThePolygon0, pointThatDefinesThePolygon1);
      }
   }

   private void assertEqualsInEitherOrder(Point2d expected0, Point2d expected1, Point2d actual0, Point2d actual1)
   {
      if (expected0.equals(actual0))
         assertTrue(expected1.equals(actual1));
      else if (expected0.equals(actual1))
         assertTrue(expected1.equals(actual0));
      else
      {
         fail("Points are not equal in either order.");
      }
   }

   private void assertEqualsInAnyOrder(Point2d expected0, Point2d expected1, Point2d expected2, Point2d actual0, Point2d actual1, Point2d actual2)
   {
      if (expected0.equals(actual0) && expected1.equals(actual1))
         assertTrue(expected2.equals(actual2));
      else if (expected0.equals(actual0) && expected1.equals(actual2))
         assertTrue(expected2.equals(actual1));
      else if (expected0.equals(actual1) && expected1.equals(actual0))
         assertTrue(expected2.equals(actual2));
      else if (expected0.equals(actual1) && expected1.equals(actual2))
         assertTrue(expected2.equals(actual0));
      else if (expected0.equals(actual2) && expected1.equals(actual0))
         assertTrue(expected2.equals(actual1));
      else if (expected0.equals(actual2) && expected1.equals(actual1))
         assertTrue(expected2.equals(actual0));
      else
         fail("Points are not equal in any order");
   }

   private void assertEqualsInEitherOrder(double expected0, double expected1, double actual0, double actual1)
   {
      if (expected0 == actual0)
         assertTrue(expected1 == actual1);
      else if (expected0 == actual1)
         assertTrue(expected1 == actual0);
      else
      {
         System.out.println(expected0);
         System.out.println(expected1);
         System.out.println(actual0);
         System.out.println(actual1);
         fail("Doubles are not equal in either order.");
      }
   }

   private void assertEqualsInEitherOrder(Line2d expected0, Line2d expected1, Line2d actual0, Line2d actual1)
   {
      if ((expected0.getSlope() == actual0.getSlope()) && (expected0.getPoint().equals(actual0.getPoint())))
         assertTrue((expected1.getSlope() == actual1.getSlope()) && (expected1.getPoint().equals(actual1.getPoint())));
      else if ((expected0.getSlope() == actual1.getSlope()) && (expected0.getPoint().equals(actual1.getPoint())))
         assertTrue((expected1.getSlope() == actual0.getSlope()) && (expected1.getPoint().equals(actual0.getPoint())));
      else
         fail("Lines are not equal in either order.");
   }

   private void pauseOneSecond()
   {
      try
      {
         Thread.sleep(1000);
      }
      catch (InterruptedException ex)
      {
      }
   }

   private void verifyEpsilonEquals(FramePoint2d point1, FramePoint2d point2)
   {
      if (point1.distance(point2) > 1e-7)
         throw new RuntimeException("point1 = " + point1 + ", point2 = " + point2);
   }
}
