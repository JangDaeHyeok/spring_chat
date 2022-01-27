package jdh.example.chat.model.dao;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import jdh.example.chat.model.dto.UserTbDTO;

@Mapper
public interface UserTbMapper {

	List<UserTbDTO> selectUserTbList(UserTbDTO userTbDTO) throws Exception;
	
	void insertUserTb(UserTbDTO userTbDTO) throws Exception;
}
