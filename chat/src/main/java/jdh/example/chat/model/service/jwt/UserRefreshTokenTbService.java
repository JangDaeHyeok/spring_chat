package jdh.example.chat.model.service.jwt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jdh.example.chat.model.dao.jwt.UserRefreshTokenTbMapper;
import jdh.example.chat.model.dto.jwt.UserRefreshTokenTbDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRefreshTokenTbService {
	@Autowired UserRefreshTokenTbMapper userRefreshTokenTbMapper;
	
	public UserRefreshTokenTbDTO getRefreshToken(UserRefreshTokenTbDTO userRefreshTokenTbDTO) throws Exception {
		List<UserRefreshTokenTbDTO> list = userRefreshTokenTbMapper.selectUserRefreshTokenTbList(userRefreshTokenTbDTO);
		
		return list.get(0);
	}
	
	public void addRefreshToken(UserRefreshTokenTbDTO userRefreshTokenTbDTO) throws Exception {
		int count = userRefreshTokenTbMapper.selectUserRefreshTokenTbCount(userRefreshTokenTbDTO);
		
		// 이미 토큰이 존재하는 경우 update
		if(count > 0) {
			userRefreshTokenTbMapper.updateUserRefreshTokenTb(userRefreshTokenTbDTO);
		}
		// 토큰이 존재하지 않는 경우 insert
		else {
			userRefreshTokenTbMapper.insertUserRefreshTokenTb(userRefreshTokenTbDTO);
		}
	}
	
	public int editRefreshToken(UserRefreshTokenTbDTO userRefreshTokenTbDTO) throws Exception {
		return userRefreshTokenTbMapper.updateUserRefreshTokenTb(userRefreshTokenTbDTO);
	}
	
	public int delRefreshToken(int userRefreshTokenIdx) throws Exception {
		return userRefreshTokenTbMapper.deleteUserRefreshTokenTb(userRefreshTokenIdx);
	}
}