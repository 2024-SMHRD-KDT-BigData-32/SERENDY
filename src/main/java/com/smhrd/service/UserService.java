package com.smhrd.service;

import com.smhrd.DTO.EditRequest;
import com.smhrd.entity.UserInfo;

public interface UserService {
    boolean register(UserInfo userInfo);
    boolean login(String id, String pw);
	boolean deleteUser(String id);
	
	UserInfo getUser(String id);
	boolean updateUserProfile(String id, EditRequest editRequest);
}
