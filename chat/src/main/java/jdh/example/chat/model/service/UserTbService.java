package jdh.example.chat.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jdh.example.chat.model.dao.UserTbMapper;
import jdh.example.chat.model.dto.UserTbDTO;
import jdh.example.chat.util.encript.SHA256;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserTbService {
	@Autowired UserTbMapper userTbMapper;
	
	public List<UserTbDTO> getUserTbList(UserTbDTO userTbDTO) throws Exception {
		return userTbMapper.selectUserTbList(userTbDTO);
	}
	
	public UserTbDTO getUserTbOne(int userIdx) throws Exception {
		UserTbDTO userTbDTO = new UserTbDTO();
		userTbDTO.setUserIdx(userIdx);
		
		return userTbMapper.selectUserTbList(userTbDTO).get(0);
	}
	
	public void addUserTb(UserTbDTO userTbDTO) throws Exception {
		// 비밀번호 암호화
		String userSalt = SHA256.getSalt();
		userTbDTO.setSalt(userSalt);
		userTbDTO.setUserPw(SHA256.encrypt(userTbDTO.getUserPw(), userSalt));
				
		userTbMapper.insertUserTb(userTbDTO);
	}
}