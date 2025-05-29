package com.smhrd.DTO;

public class LoginRequest {
    private String id;
    private String pw;

    // 기본 생성자 (필수)
    public LoginRequest() {}

    // getter, setter
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPw() {
        return pw;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }
}
