package org.bonej.ops.iterableIntervalIsBinary;

import net.imagej.ops.Op;
import net.imagej.ops.OpEnvironment;
import net.imglib2.IterableInterval;
import net.imglib2.type.BooleanType;
import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An Op which checks whether the given IterableInterval is binary.
 * A binary interval contains only two distinct values.
 *
 * @author Richard Domander
 */
@Plugin(type = Op.class, name = "iterableIntervalIsBinary")
public class IterableIntervalIsBinary implements Op {
    @Parameter(type = ItemIO.INPUT)
    private IterableInterval interval = null;

    /** If true, then the interval contains only one or two distinct values */
    @Parameter(type = ItemIO.OUTPUT)
    private boolean isBinary = false;

    public boolean isBinary() {
        return isBinary;
    }

    public void setIterableInterval(final IterableInterval interval) {
        checkNotNull(interval, "IterableInterval can't be set null");

        this.interval = interval;
    }

    @Override
    public OpEnvironment ops() {
        return null;
    }

    @Override
    public void setEnvironment(OpEnvironment opEnvironment) {

    }

    @Override
    public void run() {
        checkNotNull(interval, "IterableInterval is null. Can't run");

        isBinary = isBinaryType();
    }

    private boolean isBinaryType() {
        if (interval.size() == 0) {
            return false;
        }

        final Object element = interval.firstElement();
        final Class datasetTypeClass = element.getClass();
        return BooleanType.class.isAssignableFrom(datasetTypeClass);
    }
}
