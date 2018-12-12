package io.sease.rre.server.services;

import com.fasterxml.jackson.databind.JsonNode;
import io.sease.rre.core.domain.Evaluation;
import io.sease.rre.server.domain.EvaluationMetadata;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * An EvaluationHandlerService can be used to process an incoming evaluation
 * update request. It should extract the relevant details from the request,
 * and use them to build an Evaluation object that can be used to populate
 * the dashboard.
 * <p>
 * The {@link #processEvaluationRequest(JsonNode)} method should ideally
 * return as quickly as possible, to avoid blocking the sender of the incoming
 * request. The evaluation data can then be retrieved using {@link #getEvaluation()}
 * where the evaluation contains the most recently processed data.
 *
 * @author Matt Pearce (matt@flax.co.uk)
 */
@Service
public interface EvaluationHandlerService {

    /**
     * Update the currently held evaluation data. This may be done
     * asynchronously - the method should return as quickly as possible.
     *
     * @param requestData incoming data giving details of evaluation.
     * @throws EvaluationHandlerException if the data cannot be processed.
     */
    void processEvaluationRequest(final JsonNode requestData) throws EvaluationHandlerException;

    /**
     * Get the current evaluation data.
     *
     * @return the Evaluation.
     */
    Evaluation getEvaluation();

    /**
     * Get the current evaluation metadata.
     *
     * @return the evaluation metadata.
     */
    EvaluationMetadata getEvaluationMetadata();

    /**
     * Get the metrics in the evaluation data.
     *
     * @return a list of metric names (not sanitised). Never {@code null.}
     * @throws EvaluationHandlerException if problems occur extracting the
     *                                    metrics from the data.
     */
    List<String> getMetrics() throws EvaluationHandlerException;

    /**
     * Get the available corpus names in the evaluation data.
     *
     * @return the corpus names. Never {@code null}.
     * @throws EvaluationHandlerException if problems occur extracting the
     *                                    names from the data.
     */
    List<String> getCorpusNames() throws EvaluationHandlerException;

    /**
     * Get the available topics associated with a given corpus.
     *
     * @param corpus the corpus whose evaluation topics are required.
     * @return the topic names. Never {@code null}.
     * @throws EvaluationHandlerException if problems occur extracting the
     *                                    names from the data.
     */
    List<String> getTopicNames(String corpus) throws EvaluationHandlerException;
}
