package com.smhrd.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smhrd.repository.CbfProdRepository;
import com.smhrd.DTO.CbfProdResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class CbfServiceImpl implements CbfService{

    private final CbfProdRepository cbfProdRepository;
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 파싱용

    @Autowired
    public CbfServiceImpl(CbfProdRepository cbfProdRepository) {
        this.cbfProdRepository = cbfProdRepository;
    }

    public List<CbfProdResponse> recommendProductsByStyle(List<String> preferredStyles, List<Integer> dislikedProdIds, int limit) {
        float[] userVector = buildUserVector(preferredStyles);

        try {
            String queryJson = buildQueryJson(userVector, dislikedProdIds, limit);
            String responseJson = cbfProdRepository.searchSimilarProducts(queryJson);
            return parseEsResponse(responseJson, limit);
        } catch (IOException e) {
            throw new RuntimeException("Elasticsearch 검색 중 오류 발생", e);
        }
    }


    private float[] buildUserVector(List<String> preferredStyles) {
        String[] styleCodeList = { "트레디셔널", "매니시", "페미닌", "에스닉", "컨템포러리", "내추럴", "젠더플루이드", "스포티", "서브컬쳐", "캐주얼" };
        float[] vector = new float[styleCodeList.length];

        for (int i = 0; i < styleCodeList.length; i++) {
            if (preferredStyles.contains(styleCodeList[i])) {
                vector[i] = 1.0f;
            }
        }

        return vector;
    }

    private String buildQueryJson(float[] vector, List<Integer> dislikedProdIds, int limit) {
        StringBuilder vectorSb = new StringBuilder("[");
        for (int i = 0; i < vector.length; i++) {
            vectorSb.append(vector[i]);
            if (i < vector.length - 1) vectorSb.append(",");
        }
        vectorSb.append("]");

        StringBuilder dislikedSb = new StringBuilder("[");
        for (int i = 0; i < dislikedProdIds.size(); i++) {
            dislikedSb.append(dislikedProdIds.get(i));
            if (i < dislikedProdIds.size() - 1) dislikedSb.append(",");
        }
        dislikedSb.append("]");

        return """
        {
          "size": %d,
          "query": {
            "bool": {
              "must": {
                "knn": {
                  "style_vector": {
                    "vector": %s,
                    "k": %d
                  }
                }
              },
              "must_not": {
                "terms": {
                  "prod_id": %s
                }
              }
            }
          }
        }
        """.formatted(limit, vectorSb.toString(), limit, dislikedSb.toString());
    }


    private List<CbfProdResponse> parseEsResponse(String json, int limit) throws IOException {
        JsonNode root = objectMapper.readTree(json);
        JsonNode hits = root.path("hits").path("hits");

        List<CbfProdResponse> results = new ArrayList<>();
        for (JsonNode hit : hits) {
            if (results.size() >= limit) break;

            JsonNode source = hit.get("_source");
            CbfProdResponse item = objectMapper.treeToValue(source, CbfProdResponse.class);
            float score = (float) hit.path("_score").asDouble(0.0);
            item.setScore(score);

            results.add(item);
        }

        return results;
    }

}
