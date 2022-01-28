package jdh.example.chat.model.dao.user;


import org.apache.ibatis.annotations.Mapper;

import jdh.example.chat.model.dto.user.UserRegistTbDTO;

@Mapper
public interface UserRegistTbMapper {
	int selectUserDuplCheck(String userId) throws Exception;
	
	void insertUserTb(UserRegistTbDTO userRegistTbDTO) throws Exception;
}
