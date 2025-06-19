package com.smhrd.DTO;

import java.util.List;
import lombok.Data;

@Data
public class ProfileRequest {
    private String age;
    private List<String> style;
    private List<String> fit;
    private List<String> color;
    private List<String> candidate_items;
}
