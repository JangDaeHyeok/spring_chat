package jdh.example.chat.model.dao.user;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import jdh.example.chat.model.dto.user.UserTbDTO;

@Mapper
public interface UserTbMapper {
	List<UserTbDTO> selectUserTbList(UserTbDTO userTbDTO) throws Exception;
	
	List<UserTbDTO> selectUserTbListForLogin(UserTbDTO userTbDTO) throws Exception;
}
