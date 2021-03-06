package io.sease.rre.core.domain.metrics.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.sease.rre.core.BaseTestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import static io.sease.rre.core.TestData.A_VERSION;
import static io.sease.rre.core.TestData.TEN_SEARCH_HITS;
import static java.util.Arrays.stream;
import static org.junit.Assert.assertEquals;

/**
 * Precision@3 Test Case.
 *
 * @author agazzarini
 * @since 1.0
 */
public class PrecisionAtThreeTestCase extends BaseTestCase {
    /**
     * Setup fixture for this test case.
     */
    @Before
    public void setUp() {
        cut = new PrecisionAtThree();
        cut.setVersions(Collections.singletonList(A_VERSION));
        counter = new AtomicInteger(0);
    }

    /**
     * If all results in the window are relevant, then the precision is 1.
     */
    @Test
    public void maximumPrecision() {
       maximum();
    }

    /**
     * First, second and third are relevant results.
     */
    @Test
    public void _4_judgments_10_search_results_4_relevant_results_at_top() {
        final ObjectNode judgements = mapper.createObjectNode();
        stream(TEN_SEARCH_HITS).limit(4).forEach(docid -> judgements.set(docid, createJudgmentNode(3)));
        cut.setRelevantDocuments(judgements);

        cut.setTotalHits(TEN_SEARCH_HITS.length, A_VERSION);
        stream(TEN_SEARCH_HITS)
                .map(this::searchHit)
                .forEach(hit -> cut.collect(hit, counter.incrementAndGet(), A_VERSION));

        assertEquals(
                1,
                cut.valueFactory(A_VERSION).value().doubleValue(),
                0.001);
    }

    /**
     * Only the 1st relevant result.
     */
    @Test
    public void _only_1st_relevant_result() {
        final ObjectNode judgements = mapper.createObjectNode();
        stream(TEN_SEARCH_HITS).limit(1).forEach(docid -> judgements.set(docid, createJudgmentNode(3)));
        cut.setRelevantDocuments(judgements);

        cut.setTotalHits(TEN_SEARCH_HITS.length, A_VERSION);
        stream(TEN_SEARCH_HITS)
                .map(this::searchHit)
                .forEach(hit -> cut.collect(hit, counter.incrementAndGet(), A_VERSION));

        assertEquals(
                0.33,
                cut.valueFactory(A_VERSION).value().doubleValue(),
                0.001);
    }

    /**
     * Only the 2nd relevant result.
     */
    @Test
    public void _only_2nd_relevant_result() {
        final ObjectNode judgements = mapper.createObjectNode();
        stream(TEN_SEARCH_HITS).skip(1).limit(1).forEach(docid -> judgements.set(docid, createJudgmentNode(3)));
        cut.setRelevantDocuments(judgements);

        cut.setTotalHits(TEN_SEARCH_HITS.length, A_VERSION);
        stream(TEN_SEARCH_HITS)
                .map(this::searchHit)
                .forEach(hit -> cut.collect(hit, counter.incrementAndGet(), A_VERSION));

        assertEquals(
                0.33,
                cut.valueFactory(A_VERSION).value().doubleValue(),
                0.001);
    }

    /**
     * Only the 1st and 2nd relevant result.
     */
    @Test
    public void _only_1st_and_2nd_relevant_result() {
        final ObjectNode judgements = mapper.createObjectNode();
        stream(TEN_SEARCH_HITS).limit(2).forEach(docid -> judgements.set(docid, createJudgmentNode(3)));
        cut.setRelevantDocuments(judgements);

        cut.setTotalHits(TEN_SEARCH_HITS.length, A_VERSION);
        stream(TEN_SEARCH_HITS)
                .map(this::searchHit)
                .forEach(hit -> cut.collect(hit, counter.incrementAndGet(), A_VERSION));

        assertEquals(
                0.67,
                cut.valueFactory(A_VERSION).value().doubleValue(),
                0.001);
    }


    /**
     * Only the 3rd relevant result.
     */
    @Test
    public void _only_3rd_relevant_result() {
        final ObjectNode judgements = mapper.createObjectNode();
        stream(TEN_SEARCH_HITS).skip(2).limit(1).forEach(docid -> judgements.set(docid, createJudgmentNode(3)));
        cut.setRelevantDocuments(judgements);

        cut.setTotalHits(TEN_SEARCH_HITS.length, A_VERSION);
        stream(TEN_SEARCH_HITS)
                .map(this::searchHit)
                .forEach(hit -> cut.collect(hit, counter.incrementAndGet(), A_VERSION));

        assertEquals(
                0.33,
                cut.valueFactory(A_VERSION).value().doubleValue(),
                0.001);
    }

    /**
     * Only the 3rd relevant result.
     */
    @Test
    public void _only_2nd_and_3rd_relevant_result() {
        final ObjectNode judgements = mapper.createObjectNode();
        stream(TEN_SEARCH_HITS).skip(1).limit(2).forEach(docid -> judgements.set(docid, createJudgmentNode(3)));
        cut.setRelevantDocuments(judgements);

        cut.setTotalHits(TEN_SEARCH_HITS.length, A_VERSION);
        stream(TEN_SEARCH_HITS)
                .map(this::searchHit)
                .forEach(hit -> cut.collect(hit, counter.incrementAndGet(), A_VERSION));

        assertEquals(
                0.67,
                cut.valueFactory(A_VERSION).value().doubleValue(),
                0.001);
    }
}