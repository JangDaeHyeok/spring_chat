package jdh.example.chat.model.dao.user;


import org.apache.ibatis.annotations.Mapper;

import jdh.example.chat.model.dto.user.UserLoginTbDTO;

@Mapper
public interface UserLoginTbMapper {
	UserLoginTbDTO selectUserLoginTbOne(UserLoginTbDTO userLoginTbDTO) throws Exception;
}
