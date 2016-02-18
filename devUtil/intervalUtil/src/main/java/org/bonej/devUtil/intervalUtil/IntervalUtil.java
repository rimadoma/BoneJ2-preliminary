package org.bonej.devUtil.intervalUtil;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.LongStream;

import javax.annotation.Nullable;

import net.imglib2.IterableRealInterval;
import net.imglib2.type.numeric.RealType;

/**
 * Utility methods for IterableInterval
 *
 * Richard Domander
 */
public final class IntervalUtil {
    private IntervalUtil() {}

    /**
     * Fills the given interval with the sequence {f(seed), f(seed + 1), f(seed + 2)... f(n)},
     * where n is the number of elements in the interval
     *
     * @implNote    Does not check for under- or overflow, e.g. f(x) > m,
     *              where m == getMaxValue() for an element in the interval
     * @param seed  The first argument to the function f, i.e. starting point of the sequence
     * @param f     A function of long -> long
     */
    public static void fillIntervalWithFunction( @Nullable final IterableRealInterval<RealType<?>> interval,
                                                 final long seed, final Function<Long, Long> f) {
        if (interval == null) {
            return;
        }

        checkNotNull(f, "Cannot fill interval: function f is null");

        final Iterator<Long> longIterator = LongStream.iterate(seed, l -> l + 1).iterator();

        interval.cursor().forEachRemaining(c -> c.setReal(f.apply(longIterator.next())));
    }
}
