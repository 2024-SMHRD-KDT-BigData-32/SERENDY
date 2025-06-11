package com.smhrd.repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CbfProdRepository {

    private final RestClient restClient;

    public String searchSimilarProducts(String queryJson) throws IOException {
        Request request = new Request("POST", "/product-cbf-v1/_search");
        request.setJsonEntity(queryJson);

        Response response = restClient.performRequest(request);
        return new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
    }
}
