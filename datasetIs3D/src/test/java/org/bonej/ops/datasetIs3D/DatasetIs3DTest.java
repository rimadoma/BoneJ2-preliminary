package org.bonej.ops.datasetIs3D;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the DatasetIs3D Op
 *
 * @author Richard Domander
 */
public class DatasetIs3DTest {
    private static final ImageJ ij = new ImageJ();

    @AfterClass
    public static void oneTimeTearDown() {
        ij.context().dispose();
    }


    @Test
    public void TestImageWithLessThanThreeDimensionsFails() {
        final long[] dims = {100, 100, 10};
        final AxisType[] axisTypes = {Axes.X, Axes.Y, Axes.TIME};
        Dataset dataset = ij.dataset().create(new UnsignedByteType(), dims, "Test image", axisTypes);

        boolean result = (boolean) ij.op().run("datasetIs3D", dataset);
        assertFalse("An image with two spatial dimensions is not 3D", result);
    }

    @Test
    public void Test3DImagePasses() {
        final long[] dims = {100, 100, 100, 3};
        final AxisType[] axisTypes = {Axes.X, Axes.Y, Axes.Z, Axes.CHANNEL};
        Dataset dataset = ij.dataset().create(new UnsignedByteType(), dims, "Test image", axisTypes);

        boolean result = (boolean) ij.op().run("datasetIs3D", dataset);
        assertTrue("An image with three spatial dimensions is 3D", result);
    }


    @Test
    public void TestImageWithMoreThanThreeDimensionsFails() {
        final AxisType W = Axes.get("W", true);
        final long[] dims = {100, 100, 100, 100};
        final AxisType[] axisTypes = {Axes.X, Axes.Y, Axes.Z, W};
        Dataset dataset = ij.dataset().create(new UnsignedByteType(), dims, "Test image", axisTypes);

        boolean result = (boolean) ij.op().run("datasetIs3D", dataset);
        assertFalse("An image with four spatial dimensions is not 3D", result);
    }
}