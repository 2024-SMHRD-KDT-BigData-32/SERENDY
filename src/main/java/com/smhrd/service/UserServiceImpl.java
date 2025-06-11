package com.smhrd.service;

import com.smhrd.DTO.EditRequest;
import com.smhrd.entity.UserInfo;
import com.smhrd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean register(UserInfo userInfo) {
        // 1. 아이디 중복 체크 (이제는 id가 사용자 입력 아이디임)
        Optional<UserInfo> existingUser = userRepository.findById(userInfo.getId());
        if (existingUser.isPresent()) {
            return false; // 중복 사용자 있음
        }

        // 2. 가입일 설정
        userInfo.setJoinedAt(LocalDateTime.now());

        // 3. 저장
        userRepository.save(userInfo);

        return true;
    }
    
    @Override
    public boolean login(String id, String pw) {
        Optional<UserInfo> userOpt = userRepository.findById(id);

        if (userOpt.isPresent()) {
            UserInfo user = userOpt.get();
            return user.getPw().equals(pw); 
        }

        return false;
    }
    
    @Override
    public boolean deleteUser(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    // 유저 가져오기(마이페이지-유저 정보 조회 시 사용)
	@Override
	public UserInfo getUser(String id) {
		UserInfo existingUser = userRepository.findById(id).orElse(null); // 없을 경우 null
		return existingUser;
	}

	// 유저 정보 수정
	@Override
	public boolean updateUserProfile(String id, EditRequest editRequest) {
		UserInfo existingUser = getUser(id);
		
		if(existingUser != null) {
			
			existingUser.setPw(editRequest.getPw());
			existingUser.setName(editRequest.getName());
			existingUser.setGender(editRequest.getGender());
			existingUser.setAgeGroup(editRequest.getAgeGroup());
			
			// 해당 id의 엔티티 존재하면 update로 됨
			userRepository.save(existingUser);
			return true;
		} else {
			return false;
		}
		
	}

    
}

