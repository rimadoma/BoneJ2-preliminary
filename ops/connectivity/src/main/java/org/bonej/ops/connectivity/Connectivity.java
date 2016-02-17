package org.bonej.ops.connectivity;

import net.imagej.Dataset;
import net.imagej.ops.Op;
import net.imagej.ops.OpEnvironment;
import org.bonej.ops.datasetIs3D.DatasetIs3D;
import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An Op which determines the number of connected structures in a Dataset by calculating the Euler characteristic.
 * The Euler characteristic is determined from voxel neighborhoods.
 * The Op assumes that there is only one continuous foreground structure in the Dataset.
 *
 * @author Richard Domander
 */
@Plugin(type = Op.class, name = "eulerConnectivity")
public class Connectivity implements Op {
    @Parameter(type = ItemIO.INPUT)
    private Dataset dataset = null;

    @Override
    public OpEnvironment ops() {
        return null;
    }

    @Override
    public void setEnvironment(OpEnvironment opEnvironment) {

    }

    @Override
    public void run() throws NullPointerException, IllegalArgumentException {
        checkDataset();
    }

    private void checkDataset() throws NullPointerException, IllegalArgumentException {
        checkNotNull(dataset, "Dataset is null. Can't run without a Dataset");
        checkArgument(checkIs3D(), "Image must be three dimensional");
    }

    private boolean checkIs3D() {
        DatasetIs3D datasetIs3D = new DatasetIs3D();
        datasetIs3D.setDataset(dataset);
        datasetIs3D.setStrictlySpatial(true);
        datasetIs3D.run();
        return datasetIs3D.is3D();
    }
}
