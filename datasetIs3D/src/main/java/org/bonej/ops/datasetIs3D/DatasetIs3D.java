package org.bonej.ops.datasetIs3D;

import static com.google.common.base.Preconditions.checkNotNull;

import net.imagej.Dataset;
import net.imagej.axis.CalibratedAxis;
import net.imagej.ops.Op;
import net.imagej.ops.OpEnvironment;
import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import java.util.Arrays;

/**
 * An Op which checks whether the given Dataset has three spatial dimensions
 *
 * @author Richard Domander
 */

@Plugin(type = Op.class, name = "datasetIs3D")
public class DatasetIs3D implements Op {
    private static final int SPATIAL_DIMENSIONS = 3;

    @Parameter(type = ItemIO.INPUT)
    private Dataset dataset = null;

    /** If true then the dataset must have only spatial dimensions to be 3D */
    @Parameter(type = ItemIO.INPUT, required = false)
    private boolean strictlySpatial = false;

    @Parameter(type = ItemIO.OUTPUT)
    private boolean is3D = false;

    public boolean is3D() {
        return is3D;
    }

    public void setDataset(final Dataset dataset) throws NullPointerException {
        checkNotNull(dataset, "Dataset can't be set null");

        this.dataset = dataset;
    }

    public void setStrictlySpatial(final boolean strict) {
        strictlySpatial = strict;
    }

    @Override
    public OpEnvironment ops() {
        return null;
    }

    @Override
    public void setEnvironment(OpEnvironment opEnvironment) {

    }

    @Override
    public void run() throws NullPointerException {
        checkNotNull(dataset, "Dataset is null. Can't run without a Dataset");

        is3D = false;

        final int numDimensions = dataset.numDimensions();
        if (numDimensions < SPATIAL_DIMENSIONS) {
            return;
        }

        final CalibratedAxis[] axes = new CalibratedAxis[numDimensions];
        dataset.axes(axes);
        long spatialAxisCount = Arrays.stream(axes).filter(axis -> axis.type().isSpatial()).count();
        is3D = spatialAxisCount == SPATIAL_DIMENSIONS && (numDimensions == SPATIAL_DIMENSIONS || !strictlySpatial);
    }
}
