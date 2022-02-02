package jdh.example.chat.controller.chat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import jdh.example.chat.model.dto.chat.ChatMsgDTO;
import jdh.example.chat.model.repository.ChatRoomRepository;
import jdh.example.chat.model.service.chat.ChatService;
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
	@Autowired JwtTokenProvider jwtTokenProvider;
	@Autowired ChatRoomRepository chatRoomRepository;
	@Autowired ChatService chatService;
	
	// WebSocket /pub/chat/message 로 들어오는 메시지 처리
	@MessageMapping("/chat/message")
	public void message(ChatMsgDTO chatMsgDTO, HttpServletRequest req) throws Exception {
		// JWT 토큰 정보를 이용한 message sender 설정
		chatMsgDTO.setSender(jwtTokenProvider.getUsernameFromToken(req.getHeader("Authorization")));
		
		// 채팅방 인원수 세팅
		chatMsgDTO.setUserCount(chatRoomRepository.getUserCount(chatMsgDTO.getRoomId()));
		
		// Websocket에 발행된 메시지를 redis로 발행(publish)
		try {
			chatService.sendChatMessage(chatMsgDTO);
		} catch (NullPointerException e) {
			log.error("[ChatController] 채팅 전송 중 문제 발생 : ", e.getMessage());
		}
	}
}
