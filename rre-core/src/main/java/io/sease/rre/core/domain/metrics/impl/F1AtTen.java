package io.sease.rre.core.domain.metrics.impl;

/**
 * The most popular F-Measure, which balances between precisin and recall using 1 as beta factor.
 *
 * @author worleydl 
 * @since 1.1
 */
public class F1AtTen extends FMeasureAtK {
    /**
     * Builds a new F1 metric instance.
     */
    public F1AtTen() {
        super("F1@10", 1, 10);
    }
}
