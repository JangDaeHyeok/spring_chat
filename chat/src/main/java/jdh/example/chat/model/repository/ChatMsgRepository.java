//package jdh.example.chat.model.repository;
//
//import javax.annotation.Resource;
//
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.stereotype.Repository;
//
//import jdh.example.chat.model.dto.chat.ChatMsgDTO;
//import lombok.RequiredArgsConstructor;
//
//@RequiredArgsConstructor
//@Repository
//public class ChatMsgRepository {
//	private static final String ROOM_MSG = "ROOM_MSG";
//	
//	@Resource(name = "redisTemplate")
//	private HashOperations<String, String, ChatMsgDTO> opsHashChatMsg;
//	
//	// 유저가 입장한 채팅방ID와 유저 세션ID 맵핑 정보 저장
//	public void putChatMsgInfo(ChatMsgDTO chatMsgDTO) {
//		opsHashChatMsg.put(ROOM_MSG + chatMsgDTO.getRoomId(), chatMsgDTO.getRoomId(), chatMsgDTO);
//	}
//}
