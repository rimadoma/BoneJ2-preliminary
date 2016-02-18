package org.bonej.devUtil.intervalUtil;

import static org.bonej.devUtil.datasetCreator.DatasetCreator.DatasetType;
import static org.junit.Assert.assertArrayEquals;

import java.util.function.Function;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;

import org.bonej.devUtil.datasetCreator.DatasetCreator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for the IntervalUtil class
 *
 * @author Richard Domander
 */
public class IntervalUtilTest {
    private static final ImageJ ij = new ImageJ();
    private static final DatasetCreator datasetCreator = new DatasetCreator();

    @BeforeClass
    public static void oneTimeSetUp() {
        datasetCreator.setContext(ij.getContext());
    }

    @AfterClass
    public static void oneTimeTearDown() {
        ij.context().dispose();
    }

    @Test(expected = NullPointerException.class)
    public void testFillIntervalWithFunctionThrowsNullPointerExceptionIfFunctionIsNull() {
        Dataset interval = datasetCreator.createDataset(DatasetType.LONG);
        IntervalUtil.fillIntervalWithFunction(interval, 1, null);
    }

    @Test
    public void testFillIntervalWithFunction() {
        final long[] expectedSequence = {2, 4, 6, 8};
        final long sequenceLength = expectedSequence.length;
        final long seed = 1;
        Function<Long, Long> function = x -> x * 2;

        Dataset interval = datasetCreator.createDataset(DatasetType.LONG, new AxisType[]{Axes.X},
                new long[]{sequenceLength});
        IntervalUtil.fillIntervalWithFunction(interval, seed, function);

        long[] sequence = (long[]) interval.getPlane(0);
        assertArrayEquals("Incorrect sequence", expectedSequence, sequence);
    }
}