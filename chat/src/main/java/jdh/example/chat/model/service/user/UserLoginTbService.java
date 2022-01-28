package jdh.example.chat.model.service.user;

import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import jdh.example.chat.model.dao.user.UserLoginTbMapper;
import jdh.example.chat.model.dto.user.UserLoginTbDTO;
import jdh.example.chat.util.encript.SHA256;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLoginTbService {
	@Autowired UserLoginTbMapper userLoginTbMapper;
	
	// 사용자 로그인
	public UserLoginTbDTO getUserLoginTbOne(String userId, String userPw) throws Exception {
		UserLoginTbDTO userLoginTbDTO = new UserLoginTbDTO();
		userLoginTbDTO.setUserId(userId);
		userLoginTbDTO = userLoginTbMapper.selectUserLoginTbOne(userLoginTbDTO);
		
		// 존재하지 않는 사용자인 경우
		if(userLoginTbDTO == null || userLoginTbDTO.getUserId() == null) {
			throw new NotFoundException("존재하지 않는 아이디");
		}
		
		// 비밀번호 일치 여부 체크
		log.info("[UserLoginTbService] 입력 비밀번호 암호화 :: {}", SHA256.encrypt(userPw, userLoginTbDTO.getSalt()));
		log.info("[UserLoginTbService] 저장된 비밀번호 :: {}", userLoginTbDTO.getUserPw());
		if(!SHA256.encrypt(userPw, userLoginTbDTO.getSalt()).equals(userLoginTbDTO.getUserPw())) {
			throw new BadCredentialsException(userLoginTbDTO.getUserId() + " Invalid password");
		}
		
		return userLoginTbDTO;
	}
}