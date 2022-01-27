package jdh.example.chat.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jdh.example.chat.model.dao.UserTbMapper;
import jdh.example.chat.model.dto.UserTbDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserTbService {
	@Autowired UserTbMapper userTbMapper;
	
	public List<UserTbDTO> getUserTbList(UserTbDTO userTbDTO) throws Exception {
		return userTbMapper.selectUserTbList(userTbDTO);
	}
	
	public UserTbDTO getUserTbOne(String userId) throws Exception {
		UserTbDTO userTbDTO = new UserTbDTO();
		userTbDTO.setUserId(userId);
		
		return userTbMapper.selectUserTbList(userTbDTO).get(0);
	}
	
	public void addUserTb(UserTbDTO userTbDTO) throws Exception {
		userTbMapper.insertUserTb(userTbDTO);
	}
}