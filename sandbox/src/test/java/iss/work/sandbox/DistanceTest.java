package iss.work.sandbox;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DistanceTest {

    @Test
    public void testDistanceSelf() {
        Point p = new Point(0,0);
        Assert.assertEquals(p.distance(p), 0.0);
    }

    @Test
    public void testDistance() {
        Point p1 = new Point(0,0);
        Point p2 = new Point(1,0);
        Assert.assertEquals(p1.distance(p2), 1.0);
    }
}
