package org.bonej.ops.testImageCreators;

import net.imagej.Dataset;
import net.imagej.DatasetService;
import net.imagej.ImageJ;
import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;
import net.imagej.ops.Op;
import net.imagej.ops.OpEnvironment;
import net.imagej.ops.OpService;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import org.scijava.ItemIO;
import org.scijava.cache.CacheService;
import org.scijava.convert.ConvertService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An Op which creates a Dataset containing an image of a wire-frame cuboid.
 * Can be used, e.g. for testing other Ops or Plugins.
 *
 * @todo How to produce a BitType Dataset?
 * @author Richard Domander
 */
@Plugin(type = Op.class, name = "wireFrameCuboidCreator")
public class WireFrameCuboidCreator implements Op {
    private static final AxisType[] cuboidAxes = {Axes.X, Axes.Y, Axes.Z};

    @Parameter
    private DatasetService datasetService = null;

    @Parameter
    private ConvertService convertService = null;

    @Parameter
    private OpService opService = null;

    @Parameter(type = ItemIO.INPUT, min = "1", required = false, description = "Cuboid width (px)")
    private int width = 100;

    @Parameter(type = ItemIO.INPUT, min = "1", required = false, description = "Cuboid height (px)")
    private int height = 100;

    @Parameter(type = ItemIO.INPUT, min = "1", required = false, description = "Cuboid depth (px)")
    private int depth = 100;

    @Parameter(type = ItemIO.INPUT, min = "0", required = false, description = "Empty space around the cuboid (px)")
    private int padding = 10;

    @Parameter(type = ItemIO.OUTPUT)
    private Dataset dataset = null;

    @Override
    public OpEnvironment ops() {
        return null;
    }

    @Override
    public void setEnvironment(OpEnvironment opEnvironment) {

    }

    @Override
    public void run() {
        checkNotNull(datasetService, "No dataset service found");

        createWireFrameCuboid();
    }

    public static void main(String... args) {
        final ImageJ ij = net.imagej.Main.launch(args);
        Object cuboid = ij.op().run(WireFrameCuboidCreator.class);
        ij.ui().show(cuboid);
    }

    //region -- Helper methods --
    private void createWireFrameCuboid() {
        final int totalPadding = 2 * padding;
        final int paddedWidth = totalPadding + width;
        final int paddedHeight = totalPadding + height;
        final int paddedDepth = totalPadding + depth;

        dataset = datasetService.create(new UnsignedByteType(), new long[]{paddedWidth, paddedHeight, paddedDepth},
                "Wire-frame cuboid", cuboidAxes);

        // Draw front and back faces
        final int frontFaceZ = padding;
        drawRect((byte[]) dataset.getPlane(frontFaceZ), paddedWidth, paddedHeight, padding);
        final int backFaceZ = padding + depth - 1;
        drawRect((byte[]) dataset.getPlane(backFaceZ), paddedWidth, paddedHeight, padding);

        if (depth <= 2) {
            return;
        }

        // Draw edges on YZ-plane
        final int x0 = padding;
        final int y0 = padding;
        final int x1 = padding + width - 1;
        final int y1 = padding + height - 1;
        final IntStream zRange = IntStream.range(frontFaceZ + 1, backFaceZ).parallel();
        zRange.forEach( z -> {
            byte[] plane = (byte[]) dataset.getPlane(z);
            putPixel(plane, x0, y0, paddedWidth);
            putPixel(plane, x1, y0, paddedWidth);
            putPixel(plane, x1, y1, paddedWidth);
            putPixel(plane, x0, y1, paddedWidth);
        });
    }

    private static void putPixel(final byte[] plane, final int x, final int y, final int width) {
        plane[y * width + x] = (byte) 0xFF;
    }

    private static byte[] drawRect(final byte[] plane, final int width, final int height, final int padding) {
        final int xEnd = width - padding;

        final IntStream offsetRange = generateOffsetRange(padding, height - padding, width);
        offsetRange.forEach( o -> IntStream.range(padding, xEnd).forEach(x -> plane[o + x] = (byte) 0xFF));

        return plane;
    }

    private static IntStream generateOffsetRange(final int yStartInclusive, final int yEndExclusive, final int width) {
        return IntStream.range(yStartInclusive, yEndExclusive).map(y -> y * width).parallel();
    }
    //endregion
}
