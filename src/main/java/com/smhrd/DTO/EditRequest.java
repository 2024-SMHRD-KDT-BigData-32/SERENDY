package com.smhrd.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditRequest {

    private String pw;
    private String name;
    private String gender;
    private String ageGroup;
 
}
