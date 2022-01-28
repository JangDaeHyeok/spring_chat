package jdh.example.chat.controller.chat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import jdh.example.chat.model.dto.chat.ChatMsgDTO;
import jdh.example.chat.model.repository.ChatRoomRepository;
import jdh.example.chat.redis.RedisPublisher;
import jdh.example.chat.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 장대혁
 * @date 2022-01-15
 * @description 메시지 pub으로 들어오는 경우 처리하는 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {
	private final RedisPublisher redisPublisher;
	private final ChatRoomRepository chatRoomRepository;
	
	@Autowired JwtTokenProvider jwtTokenProvider;
	
	// WebSocket /pub/chat/message 로 들어오는 메시지 처리
	@MessageMapping("/chat/message")
	public void message(ChatMsgDTO chatMsgDTO, HttpServletRequest req) throws Exception {
		// JWT 토큰 정보를 이용한 message sender 설정
		chatMsgDTO.setSender(jwtTokenProvider.getUsernameFromToken(req.getHeader("Authorization")));
		
		if(ChatMsgDTO.MessageType.ENTER.equals(chatMsgDTO.getType())) {
			chatRoomRepository.enterChatRoom(chatMsgDTO.getRoomId());
			chatMsgDTO.setMessage(chatMsgDTO.getSender() + "님이 입장하셨습니다.");
		}
		
		try {
			// WebSocket에 발행된 메시지를 redis로 발행(publish)
			redisPublisher.publish(chatRoomRepository.getTopic(chatMsgDTO.getRoomId()), chatMsgDTO);
		} catch (NullPointerException e) {
			log.error("[ChatController] 채팅 전송 중 문제 발생 : ", e.getMessage());
		}
	}
}
