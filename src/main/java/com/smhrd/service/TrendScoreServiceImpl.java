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
public class TrendScoreServiceImpl implements TrendScoreService{

	private final RestClient restClient;  // ← 수정됨

	public TrendScoreServiceImpl(RestClient restClient) {
	    this.restClient = restClient;
	}

	@Override
	public List<TrendScoreDto> getTopTrendScores(List<Integer> prodIds) {
	    String idsJsonArray = prodIds.stream()
	        .map(String::valueOf)
	        .collect(Collectors.joining(","));
	
	    String queryJson = """
	    {
	      "query": {
	        "terms": {
	          "prod_id": [%s]
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
	
	        // JSON 파싱 (예: Jackson)
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
	    } catch (IOException e) {
	        throw new RuntimeException("Elasticsearch 요청 실패", e);
	    }
	}

}
