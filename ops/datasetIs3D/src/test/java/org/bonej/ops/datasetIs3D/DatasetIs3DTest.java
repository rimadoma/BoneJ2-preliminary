package org.bonej.ops.datasetIs3D;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;
import org.bonej.devUtil.datasetCreator.DatasetCreator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
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
    private static final DatasetCreator datasetCreator = new DatasetCreator();

    @BeforeClass
    public static void oneTimeSetUp() { datasetCreator.setContext(ij.getContext()); }

    @AfterClass
    public static void oneTimeTearDown() {
        ij.context().dispose();
    }

    @Test(expected = NullPointerException.class)
    public void TestSetDatasetThrowsNullPointerExceptionIfArgumentIsNull() {
        DatasetIs3D datasetIs3D = new DatasetIs3D();
        datasetIs3D.setDataset(null);
    }

    @Test(expected = NullPointerException.class)
    public void TestRunThrowsNullPointerExceptionIfDatasetIsNotSet() {
        DatasetIs3D datasetIs3D = new DatasetIs3D();
        datasetIs3D.run();
    }


    @Test
    public void TestImageWithLessThanThreeDimensionsFails() {
        final long[] dims = {100, 100, 10};
        final AxisType[] axisTypes = {Axes.X, Axes.Y, Axes.TIME};
        Dataset dataset = datasetCreator.createDataset(DatasetCreator.DatasetType.BIT, axisTypes, dims);

        final boolean result = (boolean) ij.op().run("datasetIs3D", dataset, false);
        assertFalse("An image with two spatial dimensions is not 3D", result);
    }

    @Test
    public void Test3DImagePasses() {
        final long[] dims = {100, 100, 100, 3};
        final AxisType[] axisTypes = {Axes.X, Axes.Y, Axes.Z, Axes.CHANNEL};
        Dataset dataset = datasetCreator.createDataset(DatasetCreator.DatasetType.BYTE, axisTypes, dims);

        final boolean result = (boolean) ij.op().run("datasetIs3D", dataset, false);
        assertTrue("An image with three spatial dimensions is 3D", result);
    }


    @Test
    public void TestImageWithMoreThanThreeDimensionsFails() {
        final AxisType W = Axes.get("W", true);
        final long[] dims = {100, 100, 100, 100};
        final AxisType[] axisTypes = {Axes.X, Axes.Y, Axes.Z, W};
        Dataset dataset = datasetCreator.createDataset(DatasetCreator.DatasetType.SHORT, axisTypes, dims);

        final boolean result = (boolean) ij.op().run("datasetIs3D", dataset, false);
        assertFalse("An image with four spatial dimensions is not 3D", result);
    }

    @Test
    public void TestStrictlySpatial() {
        Dataset dataset = datasetCreator.createDataset(DatasetCreator.DatasetType.SHORT);

        boolean result = (boolean) ij.op().run("datasetIs3D", dataset, true);
        assertTrue("An image with exactly three spatial dimensions is 3D", result);

        final long[] extraDims = {100, 100, 100, 3};
        final AxisType[] extraAxisTypes = {Axes.X, Axes.Y, Axes.Z, Axes.CHANNEL};
        Dataset extraDataset = datasetCreator.createDataset(DatasetCreator.DatasetType.INT, extraAxisTypes, extraDims);

        result = (boolean) ij.op().run("datasetIs3D", extraDataset, true);
        assertFalse("An image with non-spatial dimensions is not strictly 3D", result);
    }
}