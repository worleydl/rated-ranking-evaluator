package io.sease.rre.core.domain.metrics.impl;

/**
 * The most popular F-Measure, which balances between precision and recall using 1 as beta factor.
 *
 * @author worleydl 
 * @since 1.1
 */
public class F1AtThree extends FMeasureAtK {
    /**
     * Builds a new F1 metric instance.
     */
    public F1AtThree() {
        super("F1@3", 1, 3);
    }
}
