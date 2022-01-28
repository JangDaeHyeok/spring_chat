package jdh.example.chat.model.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jdh.example.chat.model.dao.user.UserTbMapper;
import jdh.example.chat.model.dto.user.UserTbDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserTbService {
	@Autowired UserTbMapper userTbMapper;
	
	public List<UserTbDTO> getUserTbList(UserTbDTO userTbDTO) throws Exception {
		return userTbMapper.selectUserTbList(userTbDTO);
	}
	
	public UserTbDTO getUserTbOneByIdx(int userIdx) throws Exception {
		UserTbDTO userTbDTO = new UserTbDTO();
		userTbDTO.setUserIdx(userIdx);
		
		return userTbMapper.selectUserTbList(userTbDTO).get(0);
	}
	
	public UserTbDTO getUserTbOneById(String userId) throws Exception {
		UserTbDTO userTbDTO = new UserTbDTO();
		userTbDTO.setUserId(userId);
		
		return userTbMapper.selectUserTbList(userTbDTO).get(0);
	}
}