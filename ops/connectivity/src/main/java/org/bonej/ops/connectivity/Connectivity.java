package org.bonej.ops.connectivity;

import static com.google.common.base.Preconditions.checkArgument;

import net.imagej.Dataset;
import net.imagej.ops.Op;
import net.imagej.ops.OpEnvironment;
import net.imagej.ops.OpService;

import org.bonej.ops.datasetIs3D.DatasetIs3D;
import org.bonej.ops.iterableIntervalIsBinary.IterableIntervalIsBinary;
import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

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

    @Parameter
    private OpService opService;

    @Override
    public OpEnvironment ops() {
        return null;
    }

    @Override
    public void setEnvironment(OpEnvironment opEnvironment) {

    }

    @Override
    public void run() throws IllegalArgumentException {
        checkDataset();
    }

    private void checkDataset() throws IllegalArgumentException {
        checkArgument(checkIsStrictly3D(), "Dataset must be three dimensional and only spatial dimensions");
        checkArgument(checkIsBinary(), "Dataset is not a thresholded binary image");
    }

    private boolean checkIsBinary() {
        return (boolean) opService.run(IterableIntervalIsBinary.class, dataset);
    }

    private boolean checkIsStrictly3D() {
        return (boolean) opService.run(DatasetIs3D.class, dataset, true);
    }
}
