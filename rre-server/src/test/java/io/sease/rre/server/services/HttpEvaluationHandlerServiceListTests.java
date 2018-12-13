package io.sease.rre.server.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the list fetch methods in the HTTP evaluation handler
 * service.
 *
 * @author Matt Pearce (matt@flax.co.uk)
 */
public class HttpEvaluationHandlerServiceListTests {

    private static final String EXAMPLE_JSON_FILE = "/http/example_evaluation.json";

    private static JsonNode exampleJson;

    private EvaluationHandlerService handler;

    @BeforeClass
    public static void initialiseExampleJson() throws Exception {
        exampleJson = new ObjectMapper().readTree(HttpEvaluationHandlerServiceListTests.class.getResourceAsStream(EXAMPLE_JSON_FILE));
    }

    @Before
    public void setupHandler() {
        handler = new HttpEvaluationHandlerService();
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

        List<String> metrics = handler.getMetrics();
        assertThat(metrics).contains("P", "R", "RR@10", "AP", "NDCG@10", "P@1", "P@2", "P@3", "P@10");
    }

    @Test
    public void getVersionsReturnsEmptyList_whenNoEvaluationSet() throws Exception {
        List<String> versions = handler.getVersions();

        assertThat(versions).isNotNull();
        assertThat(versions).isEmpty();
    }

    @Test
    public void getVersionsReturnsExpectedList() throws Exception {
        handler.processEvaluationRequest(exampleJson);
        List<String> versions = handler.getVersions();

        assertThat(versions).contains("v1.0", "v1.1");
    }

    @Test
    public void getCorpusNamesReturnsEmptyList_whenNoEvaluationSet() throws Exception {
        List<String> corpusNames = handler.getCorpusNames();

        assertThat(corpusNames).isNotNull();
        assertThat(corpusNames).isEmpty();
    }

    @Test
    public void getCorpusNamesReturnsExpectedList() throws Exception {
        handler.processEvaluationRequest(exampleJson);

        List<String> corpusNames = handler.getCorpusNames();
        assertThat(corpusNames).containsExactly("electric_basses.bulk");
    }

    @Test
    public void getTopicNamesReturnsEmptyList_whenNoEvaluationSet() throws Exception {
        List<String> topicNames = handler.getTopicNames("corpus");

        assertThat(topicNames).isNotNull();
        assertThat(topicNames).isEmpty();
    }

    @Test
    public void getTopicNamesReturnsEmptyList_whenCorpusIsNull() throws Exception {
        handler.processEvaluationRequest(exampleJson);
        List<String> topicNames = handler.getTopicNames(null);

        assertThat(topicNames).isNotNull();
        assertThat(topicNames).isEmpty();
    }

    @Test
    public void getTopicNamesReturnsEmptyList_whenCorpusDoesNotExist() throws Exception {
        handler.processEvaluationRequest(exampleJson);
        List<String> topicNames = handler.getTopicNames("blah");

        assertThat(topicNames).isNotNull();
        assertThat(topicNames).isEmpty();
    }

    @Test
    public void getTopicNamesReturnsExpectedTopics() throws Exception {
        handler.processEvaluationRequest(exampleJson);

        List<String> topicNames = handler.getTopicNames("electric_basses.bulk");

        assertThat(topicNames).containsExactly("Fender basses", "Gibson basses");
    }

    @Test
    public void getQueryGroupNamesReturnsEmptyList_whenNoEvaluationSet() throws Exception {
        List<String> qgNames = handler.getQueryGroupNames("", "");

        assertThat(qgNames).isNotNull();
        assertThat(qgNames).isEmpty();
    }

    @Test
    public void getQueryGroupNamesReturnsEmptyList_whenCorpusIsNull() throws Exception {
        handler.processEvaluationRequest(exampleJson);
        List<String> qgNames = handler.getQueryGroupNames(null, "");

        assertThat(qgNames).isNotNull();
        assertThat(qgNames).isEmpty();
    }

    @Test
    public void getQueryGroupNamesReturnsEmptyList_whenTopicIsNull() throws Exception {
        handler.processEvaluationRequest(exampleJson);
        List<String> qgNames = handler.getQueryGroupNames("corpus", null);

        assertThat(qgNames).isNotNull();
        assertThat(qgNames).isEmpty();
    }

    @Test
    public void getQueryGroupNamesReturnsEmptyList_whenNoSuchCorpus() throws Exception {
        handler.processEvaluationRequest(exampleJson);
        List<String> qgNames = handler.getQueryGroupNames("corpus", "Fender basses");

        assertThat(qgNames).isNotNull();
        assertThat(qgNames).isEmpty();
    }

    @Test
    public void getQueryGroupNamesReturnsEmptyList_whenNoSuchTopic() throws Exception {
        handler.processEvaluationRequest(exampleJson);
        List<String> qgNames = handler.getQueryGroupNames("electric_basses.bulk", "");

        assertThat(qgNames).isNotNull();
        assertThat(qgNames).isEmpty();
    }

    @Test
    public void getQueryGroupNamesReturnsExpectedList() throws Exception {
        handler.processEvaluationRequest(exampleJson);
        List<String> qgNames = handler.getQueryGroupNames("electric_basses.bulk", "Fender basses");

        assertThat(qgNames.size()).isEqualTo(2);
        assertThat(qgNames).contains("The group tests several searches on the Fender brand", "Several searches on a given model (Jazz bass)");
    }
}
