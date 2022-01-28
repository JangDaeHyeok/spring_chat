package jdh.example.chat.model.service.user;

import org.apache.ibatis.javassist.bytecode.DuplicateMemberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jdh.example.chat.model.dao.user.UserRegistTbMapper;
import jdh.example.chat.model.dto.user.UserRegistTbDTO;
import jdh.example.chat.util.encript.SHA256;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRegistTbService {
	@Autowired UserRegistTbMapper userRegistTbMapper;
	
	public void addUserTb(UserRegistTbDTO userRegistTbDTO) throws Exception {
		// 사용자 id 중복여부 체크
		int duplCheck = userRegistTbMapper.selectUserDuplCheck(userRegistTbDTO.getUserId());
		if(duplCheck > 0) {
			throw new DuplicateMemberException("[UserRegistTbService] 아이디 중복 :: " + userRegistTbDTO.getUserId());
		}
		
		// 비밀번호 암호화
		String userSalt = SHA256.getSalt();
		userRegistTbDTO.setSalt(userSalt);
		userRegistTbDTO.setUserPw(SHA256.encrypt(userRegistTbDTO.getUserPw(), userSalt));
				
		userRegistTbMapper.insertUserTb(userRegistTbDTO);
	}
}