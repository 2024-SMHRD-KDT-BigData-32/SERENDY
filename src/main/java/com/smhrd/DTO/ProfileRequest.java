package com.smhrd.DTO;

import java.util.List;
import lombok.Data;

@Data
public class ProfileRequest {
    private String age;
    private List<String> style_list;
    private List<String> fit_list;
    private List<String> color_list;
    private int top_n;
}
