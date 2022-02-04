package jdh.example.chat.model.dao.chat;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import jdh.example.chat.model.dto.chat.ChatMsgDTO;

@Mapper
public interface ChatMsgTbMapper {
	List<ChatMsgDTO> selectChatMsgTbList(ChatMsgDTO chatMsgDTO) throws Exception;
	
	void insetChatMsgTb(ChatMsgDTO chatMsgDTO) throws Exception;
}
