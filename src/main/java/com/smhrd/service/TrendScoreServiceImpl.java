package com.smhrd.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smhrd.DTO.TrendScoreDto;

@Service
public class TrendScoreServiceImpl implements TrendScoreService {

    private final RestClient restClient;

    public TrendScoreServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    // 상품 후보군에 해당하는 trend_score top-50
    @Override
    public List<TrendScoreDto> getTopTrendScores(List<Integer> prodIds) {
        String idsJsonArray = prodIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "[", "]"));

        String queryJson = """
        {
          "query": {
            "terms": {
              "prod_id": %s
            }
          },
          "sort": [
            {
              "trend_score": {
                "order": "desc"
              }
            }
          ],
          "size": 50
        }
        """.formatted(idsJsonArray);

        try {
            Request request = new Request("POST", "/product_trend_score/_search");
            request.setJsonEntity(queryJson);

            Response response = restClient.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());

            return parseTrendScoreResponse(responseBody);

        } catch (IOException e) {
            throw new RuntimeException("Elasticsearch 요청 실패", e);
        }
    }

    // 전체 상품 대상 trend_score 기준 top N
    @Override
    public List<TrendScoreDto> getTopTrendScoresAll(int topN) {
        String queryJson = """
        {
          "query": {
            "match_all": {}
          },
          "sort": [
            {
              "trend_score": {
                "order": "desc"
              }
            }
          ],
          "size": %d
        }
        """.formatted(topN);

        try {
            Request request = new Request("POST", "/product_trend_score/_search");
            request.setJsonEntity(queryJson);

            Response response = restClient.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());

            return parseTrendScoreResponse(responseBody);

        } catch (IOException e) {
            throw new RuntimeException("Elasticsearch 요청 실패", e);
        }
    }

    // 공통 응답 파싱 로직
    private List<TrendScoreDto> parseTrendScoreResponse(String responseBody) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode hits = mapper.readTree(responseBody).path("hits").path("hits");

        List<TrendScoreDto> result = new ArrayList<>();
        for (JsonNode hit : hits) {
            JsonNode source = hit.path("_source");
            TrendScoreDto dto = new TrendScoreDto();
            dto.setProdId(source.path("prod_id").asInt());
            dto.setTrend_score((float) source.path("trend_score").asDouble());
            result.add(dto);
        }

        return result;
    }
}

