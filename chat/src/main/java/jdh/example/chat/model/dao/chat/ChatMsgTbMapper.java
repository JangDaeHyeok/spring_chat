package jdh.example.chat.model.dao.chat;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import jdh.example.chat.model.dto.chat.ChatMsgDTO;

@Mapper
public interface ChatMsgTbMapper {
	List<ChatMsgDTO> selectChatMsgTbList(Map<String, Object> input) throws Exception;
	
	void insetChatMsgTb(ChatMsgDTO chatMsgDTO) throws Exception;
}
