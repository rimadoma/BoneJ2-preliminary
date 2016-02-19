package org.bonej.devUtil.datasetUtil;

import net.imagej.Dataset;
import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Convenience methods for a Dataset
 *
 * @author Richard Domander
 */
public final class DatasetUtil {
    private DatasetUtil() {}

    public static long getWidth(Dataset dataset) {
        return getNamedAxisSize(dataset, Axes.X);
    }

    public static long getHeight(Dataset dataset) {
        return getNamedAxisSize(dataset, Axes.Y);
    }

    public static long getDepth(Dataset dataset) {
        return getNamedAxisSize(dataset, Axes.Z);
    }

    public static long getNamedAxisSize(Dataset dataset, AxisType axisType) throws NullPointerException {
        checkNotNull(dataset, "Dataset null. Can't determine axis size");
        checkNotNull(axisType, "AxisType null. Can't determine axis size");

        int index = dataset.dimensionIndex(axisType);

        if (index < 0) {
            return -1;
        }

        return dataset.dimension(index);
    }
}
