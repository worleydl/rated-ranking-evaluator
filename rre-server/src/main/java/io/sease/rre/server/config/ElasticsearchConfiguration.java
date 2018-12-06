package io.sease.rre.server.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration details for Elasticsearch.
 *
 * @author Matt Pearce (matt@flax.co.uk)
 */
@Configuration
public class ElasticsearchConfiguration {

    @Value("${elasticsearch.url}")
    private String elasticsearchUrl = "http://localhost:9200";

    @Bean
    public RestHighLevelClient buildClient() {
        return new RestHighLevelClient(RestClient.builder(HttpHost.create(elasticsearchUrl)));
    }
}
