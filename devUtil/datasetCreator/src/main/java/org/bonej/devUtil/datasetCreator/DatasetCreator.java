package org.bonej.devUtil.datasetCreator;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.LongStream;

import javax.annotation.Nullable;

import net.imagej.Dataset;
import net.imagej.DatasetService;
import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.*;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

import org.scijava.AbstractContextual;
import org.scijava.plugin.Parameter;

/**
 * A utility class that can be used to automatically create different types of Datasets.
 * Handy for, e.g. testing.
 *
 * Call setContext before creating Datasets.
 *
 * @author  Richard Domander
 */
public final class DatasetCreator extends AbstractContextual {
    private static final long DEFAULT_WIDTH = 10;
    private static final long DEFAULT_HEIGHT = 10;
    private static final long DEFAULT_DEPTH = 10;
    private static final long[] DEFAULT_DIMS = {DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_DEPTH};
    private static final AxisType[] DEFAULT_AXES = {Axes.X, Axes.Y, Axes.Z};

    @Parameter
    private DatasetService datasetService = null;

    /**
     * Creates a two dimensional empty Dataset of the given type
     * @see DatasetCreator#createDataset(DatasetType, AxisType[], long[])
     */
    public Dataset createEmptyDataset(DatasetType type) throws NullPointerException {
        return createDataset(type, new AxisType[]{Axes.X, Axes.Y}, new long[]{0, 0});
    }

    /**
     * Creates a Dataset of the given type with the default dimensions (X = 10, Y = 10, Z = 10)
     * @see DatasetCreator#createDataset(DatasetType, AxisType[], long[])
     */
    public Dataset createDataset(DatasetType type) throws NullPointerException {
        return createDataset(type, DEFAULT_AXES, DEFAULT_DIMS);
    }

    /**
     * Creates a Dataset with three spatial dimensions (X, Y, Z) of the given type.
     *
     * @throws              NullPointerException if there's no DatasetService,
     *                      or any of the arguments is null
     * @param type          The type of the Dataset - see Dataset#DatasetType
     * @param axesTypes     The types of the dimensions in the Dataset
     * @param dimensions    The sizes of the dimensions in the Dataset
     * @return A new Dataset
     */
    public Dataset createDataset(DatasetType type, AxisType[] axesTypes, long[] dimensions)
            throws NullPointerException {
        checkNotNull(datasetService, "No datasetService available - did you call setContext?");
        checkNotNull(type, "Can't create a dataset: type is null");
        checkNotNull(axesTypes, "Can't create a dataset: axesTypes is null");
        checkNotNull(dimensions, "Can't create a dataset: dimensions is null");

        switch (type) {
            case BIT:
                return datasetService.create(new BitType(), dimensions, "Dataset", axesTypes);
            case BYTE:
                return datasetService.create(new ByteType(), dimensions, "Dataset", axesTypes);
            case DOUBLE:
                return datasetService.create(new DoubleType(), dimensions, "Dataset", axesTypes);
            case FLOAT:
                return datasetService.create(new FloatType(), dimensions, "Dataset", axesTypes);
            case INT:
                return datasetService.create(new IntType(), dimensions, "Dataset", axesTypes);
            case LONG:
                return datasetService.create(new LongType(), dimensions, "Dataset", axesTypes);
            case SHORT:
                return datasetService.create(new ShortType(), dimensions, "Dataset", axesTypes);
            case UNSIGNED_128_BIT:
                return datasetService.create(new Unsigned128BitType(), dimensions, "Dataset", axesTypes);
            case UNSIGNED_12_BIT:
                return datasetService.create(new Unsigned12BitType(), dimensions, "Dataset", axesTypes);
            case UNSIGNED_2_BIT:
                return datasetService.create(new Unsigned2BitType(), dimensions, "Dataset", axesTypes);
            case UNSIGNED_4_BIT:
                return datasetService.create(new Unsigned4BitType(), dimensions, "Dataset", axesTypes);
            case UNSIGNED_BYTE:
                return datasetService.create(new UnsignedByteType(), dimensions, "Dataset", axesTypes);
            case UNSIGNED_SHORT:
                return datasetService.create(new UnsignedShortType(), dimensions, "Dataset", axesTypes);
            case UNSIGNED_INT:
                return datasetService.create(new UnsignedIntType(), dimensions, "Dataset", axesTypes);
            case UNSIGNED_LONG:
                return datasetService.create(new UnsignedLongType(), dimensions, "Dataset", axesTypes);
            case UNSIGNED_VARIABLE_BIT_LENGTH:
                return datasetService.create(new UnsignedVariableBitLengthType(64), dimensions, "Dataset", axesTypes);
            default:
                throw new AssertionError("Unhandled DatasetType value");
        }
    }

    /**
     * Fills the given interval with the sequence {f(seed), f(seed + 1), f(seed + 2)... f(n)},
     * where n is the number of elements in the interval
     *
     * @implNote    Does not check for under- or overflow, e.g. f(x) > m,
     *              where m == getMaxValue() for an element in the interval
     * @param seed  The first argument to the function f, i.e. starting point of the sequence
     * @param f     A function of long -> long
     */
    public static void fillIntervalWithFunction(@Nullable final IterableInterval interval, final long seed,
                                                final Function<Long, Long> f) {
        if (interval == null) {
            return;
        }

        final Iterator<Long> longIterator = LongStream.iterate(seed, l -> l + 1).iterator();
        final Cursor<RealType<?>> cursor = interval.cursor();

        cursor.forEachRemaining(c -> c.setReal(f.apply(longIterator.next())));
    }

    public enum DatasetType {
        BIT,
        BYTE,
        DOUBLE,
        FLOAT,
        INT,
        LONG,
        SHORT,
        UNSIGNED_128_BIT,
        UNSIGNED_12_BIT,
        UNSIGNED_2_BIT,
        UNSIGNED_4_BIT,
        UNSIGNED_BYTE,
        UNSIGNED_INT,
        UNSIGNED_LONG,
        UNSIGNED_SHORT,
        UNSIGNED_VARIABLE_BIT_LENGTH
    }
}
