package jdh.example.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import jdh.example.chat.model.dto.ChatMsgDTO;
import jdh.example.chat.model.repository.ChatRoomRepository;
import jdh.example.chat.redis.RedisPublisher;
import lombok.RequiredArgsConstructor;

/**
 * @author 장대혁
 * @date 2022-01-15
 * @description 
 */
@RequiredArgsConstructor
@Controller
public class ChatController {
	private final RedisPublisher redisPublisher;
	private final ChatRoomRepository chatRoomRepository;
	
	// WebSocket /pub/chat/message 로 들어오는 메시지 처리
	@MessageMapping("/chat/message")
	public void message(ChatMsgDTO chatMsgDTO) {
		if(ChatMsgDTO.MessageType.ENTER.equals(chatMsgDTO.getType())) {
			chatRoomRepository.enterChatRoom(chatMsgDTO.getRoomId());
			chatMsgDTO.setMessage(chatMsgDTO.getSender() + "님이 입장하셨습니다.");
		}
		
		// WebSocket에 발행된 메시지를 redis로 발행(publish)
		redisPublisher.publish(chatRoomRepository.getTopic(chatMsgDTO.getRoomId()), chatMsgDTO);
	}
}
