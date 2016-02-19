package org.bonej.ops.connectivity;

import static com.google.common.base.Preconditions.checkArgument;

import net.imagej.Dataset;
import net.imagej.legacy.translate.DatasetUtils;
import net.imagej.ops.Op;
import net.imagej.ops.OpEnvironment;
import net.imagej.ops.OpService;

import org.bonej.devUtil.datasetUtil.DatasetUtil;
import org.bonej.ops.datasetIs3D.DatasetIs3D;
import org.bonej.ops.iterableIntervalIsBinary.IterableIntervalIsBinary;
import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * An Op which determines the number of connected structures in a Dataset by calculating the Euler characteristic.
 * The Euler characteristic is determined from voxel neighborhoods.
 * The Op assumes that there is only one continuous foreground structure in the Dataset.
 *
 * @todo How to get the calibration of the input Dataset?
 * @author Richard Domander
 */
@Plugin(type = Op.class, name = "eulerConnectivity")
public class Connectivity implements Op {
    private int width = 0;
    private int height = 0;
    private int depth = 0;

    @Parameter(type = ItemIO.INPUT)
    private Dataset dataset = null;

    /** Euler characteristic of the sample as though floating in space (χ). */
    @Parameter(type = ItemIO.OUTPUT)
    private double eulerCharacteristic = 0.0;

    /** Δ(χ): the sample's contribution to the Euler characteristic of the structure to which it was connected.
     *  Calculated by counting the intersections of voxels and the edges of the image stack. */
    @Parameter(type = ItemIO.OUTPUT)
    private double deltaChi = 0.0;

    /** The connectivity of the image = 1 - Δ(χ) */
    @Parameter(type = ItemIO.OUTPUT)
    private double connectivity = 0.0;

    /** The connectivity density of the image = connectivity / sample volume */
    @Parameter(type = ItemIO.OUTPUT)
    private double connectivityDensity = 0.0;

    @Parameter
    private OpService opService = null;

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
        setDimensions();

        calculateEulerCharacteristic();
        calculateDeltaChi();
        calculateConnectivity();
        calculateConnectivityDensity();
    }

    private void setDimensions() {
        width = (int) DatasetUtil.getWidth(dataset);
        height = (int) DatasetUtil.getHeight(dataset);
        depth = (int) DatasetUtil.getDepth(dataset);
    }

    //region -- Helper methods --
    private void calculateEulerCharacteristic() {
        final int[] eulerSums = new int[depth];
        final IntStream zRange = IntStream.range(0, depth).parallel();

        zRange.forEach(z -> {
            for (int y = 0; y <= height; y++) {
                for (int x = 0; x <= width; x++) {
                    final byte[] octant = getOctant(x, y, z);
                    eulerSums[z] += getDeltaEuler(octant);
                }
            }
        });

        eulerCharacteristic = Arrays.stream(eulerSums).sum();
        eulerCharacteristic /= 8.0;
    }

    private byte[] getOctant(final int x, final int y, final int z) {
        final byte[] octant = new byte[9];

        octant[1] = getPixel(x - 1, y - 1, z - 1);
        octant[2] = getPixel(x - 1, y, z - 1);
        octant[3] = getPixel(x, y - 1, z - 1);
        octant[4] = getPixel(x, y, z - 1);
        octant[5] = getPixel(x - 1, y - 1, z);
        octant[6] = getPixel(x - 1, y, z);
        octant[7] = getPixel(x, y - 1, z);
        octant[8] = getPixel(x, y, z);

        octant[0] = countNeighbors(octant);

        return octant;
    }

    private byte getPixel(final int x, final int y, final int z) {
        if ((x < 0) || (x >= width) || (y < 0) || (y >= height) || (z < 0) || (z >= depth)) {
            return 0;
        }

        byte[] plane = (byte[]) dataset.getPlane(z);
        return plane[y * width + x];
    }

    private byte countNeighbors(byte[] octant) {
        byte neighbors = 0;

        for (int n = 1; n < octant.length; n++) {
            neighbors += octant[n];
        }

        return neighbors;
    }

    private int getDeltaEuler(final byte[] octant) {
        return 0;
    }

    private void calculateDeltaChi() {

    }

    private void calculateConnectivity() {

    }

    private void calculateConnectivityDensity() {

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
    //endregion
}
