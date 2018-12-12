package io.sease.rre.server.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the list fetch methods in the URL evaluation service.
 *
 * @author Matt Pearce (matt@flax.co.uk)
 */
public class UrlEvaluationHandlerServiceListTests {

    private static final String EXAMPLE_JSON_FILE = "/http/example_evaluation.json";

    private static JsonNode exampleJson;

    private EvaluationHandlerService handler;

    @BeforeClass
    public static void setupUrl() throws Exception {
        URL exampleJsonUrl = UrlEvaluationHandlerServiceListTests.class.getResource(EXAMPLE_JSON_FILE);
        exampleJson = new ObjectMapper().readTree("{ \"url\": \"" + exampleJsonUrl + "\" }");
    }

    @Before
    public void setupHandler() {
        handler = new URLEvaluationHandlerService();
    }

    @After
    public void tearDownHandler() {
        handler = null;
    }

    @Test
    public void getMetricsReturnsEmptyList_whenNoEvaluationSet() throws Exception {
        List<String> metrics = handler.getMetrics();

        assertThat(metrics).isNotNull();
        assertThat(metrics).isEmpty();
    }

    @Test
    public void getMetricsReturnsExpectedList() throws Exception {
        handler.processEvaluationRequest(exampleJson);
        // Sleep for long enough to read evaluation from URL
        Thread.sleep(250);

        List<String> metrics = handler.getMetrics();
        assertThat(metrics).contains("P", "R", "RR@10", "AP", "NDCG@10", "P@1", "P@2", "P@3", "P@10");
    }
}
