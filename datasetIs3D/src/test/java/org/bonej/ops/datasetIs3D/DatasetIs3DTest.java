package org.bonej.ops.datasetIs3D;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import org.junit.AfterClass;
import org.junit.Test;

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
    public void Test3DImagePasses() {
        final long[] dims = {100, 100, 100, 3};
        final AxisType[] axisTypes = {Axes.X, Axes.Y, Axes.Z, Axes.CHANNEL};
        Dataset dataset = ij.dataset().create(new UnsignedByteType(), dims, "Test image", axisTypes);

        boolean result = (boolean) ij.op().run("datasetIs3D", dataset);
        assertTrue("An image with three spatial dimensions is 3D", result);
    }
}