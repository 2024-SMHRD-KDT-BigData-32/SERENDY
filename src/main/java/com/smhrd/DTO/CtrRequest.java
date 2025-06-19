package com.smhrd.DTO;

import java.util.List;
import lombok.Data;

@Data
public class CtrRequest {
    private String userId;
    private List<Integer> candidateItems;
}
