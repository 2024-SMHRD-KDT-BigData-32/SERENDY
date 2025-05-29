package com.smhrd.controller;

import com.smhrd.DTO.LoginRequest;
import com.smhrd.entity.UserInfo;
import com.smhrd.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;


import org.springframework.web.bind.annotation.RequestBody; // 스프링용 (메서드 파라미터에만 사용)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "회원가입 및 로그인 관련 API")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
        summary = "회원가입",
        description = "사용자 정보를 받아 회원가입을 수행합니다.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "회원 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = UserInfo.class))
        )
    )
    @PostMapping("/register")
    public String registerUser(@RequestBody UserInfo userInfo) {
        System.out.println("요청받은 userInfo: " + userInfo);
        System.out.println("회원가입 요청 ID: " + userInfo.getId());
        boolean success = userService.register(userInfo);
        return success ? "회원가입 성공" : "회원가입 실패";
    }

    @Operation(
        summary = "로그인",
        description = "아이디와 비밀번호를 받아 로그인 처리합니다.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "로그인 정보",
            required = true,
            content = @Content(schema = @Schema(
                example = "{\"id\": \"testuser\", \"pw\": \"1234\"}"
            ))
        )
    )
    @PostMapping("/login")
    public String loginUser(@RequestBody LoginRequest loginRequest) {
        System.out.println("로그인 시도 id = " + loginRequest.getId());
        boolean success = userService.login(loginRequest.getId(), loginRequest.getPw());
        return success ? "로그인 성공" : "로그인 실패";
    }


    
 // UserController.java
    @DeleteMapping("/user/{id}")
    @Operation(summary = "회원 탈퇴", description = "ID에 해당하는 사용자를 삭제합니다.")
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
        boolean result = userService.deleteUser(id);
        if (result) {
            return ResponseEntity.ok("회원 탈퇴 성공");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 유저를 찾을 수 없습니다");
        }
    }


}
