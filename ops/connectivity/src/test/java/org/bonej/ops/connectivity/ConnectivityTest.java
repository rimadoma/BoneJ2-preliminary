package org.bonej.ops.connectivity;

import net.imagej.Dataset;
import net.imagej.ImageJ;

import org.bonej.ops.testImageCreators.WireFrameCuboidCreator;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the Connectivity Op
 *
 * @author Richard Domander
 */
public class ConnectivityTest {
    private static final ImageJ ij = new ImageJ();
    private static final double DELTA = 1E-12;

    /**
     * @todo How to set the calibration of a Dataset?
     */
    @Test
    public void testConnectivity() {
        final double PIXEL_VOLUME = 1.0;
        final int CUBOID_WIDTH = 32;
        final int CUBOID_HEIGHT = 64;
        final int CUBOID_DEPTH = 128;
        final int PADDING = 32;
        final int TOTAL_PADDING = 2 * PADDING;
        final int CUBOID_VOLUME = (CUBOID_WIDTH + TOTAL_PADDING) * (CUBOID_HEIGHT + TOTAL_PADDING)
                * (CUBOID_DEPTH + TOTAL_PADDING);
        final double STACK_VOLUME = CUBOID_VOLUME * PIXEL_VOLUME;
        final double EXPECTED_CONNECTIVITY = 5.0;
        final double EXPECTED_DENSITY = EXPECTED_CONNECTIVITY / STACK_VOLUME;

        Dataset cuboid = (Dataset) ij.op().run(WireFrameCuboidCreator.class, CUBOID_WIDTH, CUBOID_HEIGHT, CUBOID_DEPTH,
                PADDING);

        ArrayList<Double> results = (ArrayList<Double>) ij.op().run(Connectivity.class, cuboid);
        double eulerResult = results.get(0);
        double deltaChiResult = results.get(1);
        double connectivityResult = results.get(2);
        double densityResult = results.get(3);

        assertEquals(-4.0, eulerResult, DELTA);
        assertEquals(-4.0, deltaChiResult, DELTA);
        assertEquals(EXPECTED_CONNECTIVITY, connectivityResult, DELTA);
        assertEquals(EXPECTED_DENSITY, densityResult, DELTA);
    }
}