package org.bonej.ops.datasetIs3D;

import net.imagej.Dataset;
import net.imagej.ops.Op;
import net.imagej.ops.OpEnvironment;
import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * An Op which checks whether the given Dataset has three spatial dimensions
 *
 * @author Richard Domander
 */

@Plugin(type = Op.class, name = "datasetIs3D")
public class DatasetIs3D implements Op {
    @Parameter(type = ItemIO.INPUT)
    private Dataset dataset;

    @Parameter(type = ItemIO.OUTPUT)
    private boolean is3D;

    @Override
    public OpEnvironment ops() {
        return null;
    }

    @Override
    public void setEnvironment(OpEnvironment opEnvironment) {

    }

    @Override
    public void run() {

    }
}
