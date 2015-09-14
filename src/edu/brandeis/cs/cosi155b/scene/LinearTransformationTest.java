package edu.brandeis.cs.cosi155b.scene;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by kahliloppenheimer on 9/13/15.
 */
public class LinearTransformationTest {

    Vector ZERO_VEC = new Vector(0, 0, 0, 0);
    Random rand = new Random();

    @Test
    public void testTranslate() throws Exception {
        assertEquals(ZERO_VEC, LinearTransformation.translate(0, 0, 0).apply(ZERO_VEC));
        assertEquals(LinearTransformation.translate(1, 1, 1).apply(ZERO_VEC), ZERO_VEC);
        assertEquals(new Vector(1, 1, 1, 1), LinearTransformation.translate(1, 1, 1).apply(new Vector(0, 0, 0, 1)));

        for(int i = 0; i < 100; ++i) {
            double a = rand.nextDouble();
            double b = rand.nextDouble();
            double c = rand.nextDouble();

            double a2 = rand.nextDouble();
            double b2 = rand.nextDouble();
            double c2 = rand.nextDouble();

            Vector initial = new Vector(a, b, c, 1);
            Vector translate = new Vector(a2, b2, c2);
            LinearTransformation lt = LinearTransformation.translate(translate);

            assertEquals(new Vector(a + a2, b + b2, c + c2), lt.apply(initial));
            assertEquals(new Vector(1, 1, 1), lt.apply(new Vector(1, 1, 1, 0)));
        }

    }

    @Test
    public void testScale() throws Exception {

    }
}