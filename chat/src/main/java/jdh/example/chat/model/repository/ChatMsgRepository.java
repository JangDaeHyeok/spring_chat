package jdh.example.chat.model.repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import jdh.example.chat.model.dto.chat.ChatMsgDTO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ChatMsgRepository {
	private static final String ROOM_MSG = "ROOM_MSG";
	
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, ChatMsgDTO> opsHashChatMsg;
	
	// 유저가 입장한 채팅방ID와 유저 세션ID 맵핑 정보 저장
	public void putChatMsgInfo(ChatMsgDTO chatMsgDTO) {
		// 채팅방 생성일자
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		
		opsHashChatMsg.put(ROOM_MSG + "_" + chatMsgDTO.getRoomId(), chatMsgDTO.getRoomId() + now.format(formatter) + chatMsgDTO.getSender(), chatMsgDTO);
	}
}
