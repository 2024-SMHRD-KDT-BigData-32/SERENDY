package com.smhrd.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody; // 기능 실제 동작용 - swagger랑 헷갈리지 X!
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.DTO.EditRequest;
import com.smhrd.entity.UserInfo;
import com.smhrd.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(name = "EditProfile API", description = "개인정보 수정 API")
@CrossOrigin(origins = "*")
public class EditProfileController {
	
	private final UserService userService;
	
	public EditProfileController(UserService userService) {
		this.userService = userService;
	}
	
	// 회원 정보 조회
	@Operation(summary = "회원 정보 조회", description = "Id에 해당하는 사용자의 개인정보를 조회(확인)합니다.")
	@GetMapping("/profile/{id}")
	public ResponseEntity<?> checkProfile(@PathVariable("id") String id) {
		UserInfo user = userService.getUser(id);
		
		System.out.println("id에 해당하는 user : " + user);
		
		if(user != null) {			
			return ResponseEntity.ok(user); 
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 유저를 찾을 수 없습니다");
		}
	}
	
	// 회원 정보 수정
	@Operation(summary = "회원 정보 수정", description = "ID에 해당하는 사용자의 개인정보를 수정합니다.(아이디는 수정 X)")
	@PatchMapping("/edit/{id}")
	public ResponseEntity<String> editProfile(@PathVariable("id") String id, @RequestBody EditRequest editRequest) {
		// 회원 정보 수정 결과
		boolean result = userService.updateUserProfile(id, editRequest);
		
		if(result) {			
			System.out.println("회원 수정 정보: " + editRequest.toString());
			return ResponseEntity.ok("회원 정보 수정 완료"); 
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원 정보 수정 실패");
		}
		
	}
	

}
