package jdh.example.chat.model.dao.jwt;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import jdh.example.chat.model.dto.jwt.UserRefreshTokenTbDTO;

@Mapper
public interface UserRefreshTokenTbMapper {
	List<UserRefreshTokenTbDTO> selectUserRefreshTokenTbList(UserRefreshTokenTbDTO userRefreshTokenTbDTO) throws Exception;
	
	int selectUserRefreshTokenTbCount(UserRefreshTokenTbDTO userRefreshTokenTbDTO) throws Exception;
	
	int insertUserRefreshTokenTb(UserRefreshTokenTbDTO userRefreshTokenTbDTO) throws Exception;
	
	int updateUserRefreshTokenTb(UserRefreshTokenTbDTO userRefreshTokenTbDTO) throws Exception;
	
	int deleteUserRefreshTokenTb(int userRefreshTokenIdx) throws Exception;
	
}
