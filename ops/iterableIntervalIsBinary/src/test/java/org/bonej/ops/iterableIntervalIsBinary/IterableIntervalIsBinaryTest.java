package org.bonej.ops.iterableIntervalIsBinary;

import static org.bonej.devUtil.datasetCreator.DatasetCreator.DatasetType;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imglib2.img.planar.PlanarImg;
import net.imglib2.img.planar.PlanarImgFactory;
import net.imglib2.type.numeric.integer.ByteType;

import org.bonej.devUtil.datasetCreator.DatasetCreator;
import org.bonej.devUtil.intervalUtil.IntervalUtil;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for the DatasetIsBinary Op
 *
 * @author Richard Domander
 */
public class IterableIntervalIsBinaryTest {
    private static final ImageJ ij = new ImageJ();
    private static Dataset dataset = null;
    private static final DatasetCreator datasetCreator = new DatasetCreator();

    @BeforeClass
    public static void oneTimeSetup() {
        datasetCreator.setContext(ij.getContext());
    }

    @After
    public void tearDown() throws Exception {

    }

    @AfterClass
    public static void oneTimeTearDown() {
        ij.context().dispose();
    }


    @Test
    public void testEmptyIntervalFails() {
        Dataset dataset = datasetCreator.createEmptyDataset(DatasetType.BIT);

        final boolean result = (boolean) ij.op().run(IterableIntervalIsBinary.class, dataset);
        assertFalse("Empty interval is not binary", result);
    }

    @Test
    public void testIntervalWithOneValuePasses() {
        dataset = datasetCreator.createDataset(DatasetType.BIT);
        IntervalUtil.fillIntervalWithFunction(dataset, 0, x -> 1L);

        final boolean result = (boolean) ij.op().run(IterableIntervalIsBinary.class, dataset);

        assertTrue("An interval with one distinct value is binary", result);
    }

    @Test
    public void testIntervalWithTwoValuesPasses() {
        dataset = datasetCreator.createDataset(DatasetType.BIT);
        IntervalUtil.fillIntervalWithFunction(dataset, 0, x -> x % 2);

        final boolean result = (boolean) ij.op().run(IterableIntervalIsBinary.class, dataset);

        assertTrue("A Dataset with two distinct values is binary", result);
    }

    @Test
    public void testIntervalWithMoreThanTwoValuesFails() {
        PlanarImg planarImg = new PlanarImgFactory().create(new long[]{3}, new ByteType());
        IntervalUtil.fillIntervalWithFunction(planarImg, 0, x -> x);
        final boolean result = (boolean) ij.op().run(IterableIntervalIsBinary.class, planarImg);
        assertFalse("An interval with more than two distinct values is not binary", result);
    }

    @Test(expected = NullPointerException.class)
    public void testSetIterableIntervalThrowsNullPointerExceptionIfArgumentIsNull() {
        IterableIntervalIsBinary iterableIntervalIsBinary = new IterableIntervalIsBinary();
        iterableIntervalIsBinary.setIterableInterval(null);
    }

    @Test(expected = NullPointerException.class)
    public void testRunThrowsNullPointerExceptionIfIterableIntervalNotSet() {
        IterableIntervalIsBinary iterableIntervalIsBinary = new IterableIntervalIsBinary();
        iterableIntervalIsBinary.run();
    }
}