package jdh.example.chat.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import jdh.example.chat.model.dto.ChatMsgDTO;
import jdh.example.chat.model.dto.ChatRoomDTO;
import jdh.example.chat.model.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author 장대혁
 * @date 2022-01-11
 * @description WebSocket Client로부터 메시지를 전달받아 메시지 객체로 변환 후
 *              해당 채팅방에 접속중인 모든 WebSocket session에게 메시지 발송
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class WebSockChatHandler extends TextWebSocketHandler {
	private final ObjectMapper objectMapper;
	private final ChatService chatService;

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		log.info("payload {}", payload);
		
		/* WebSocket 접근 시 해당 Client에게 환영 메시지 전송
		TextMessage textMessage = new TextMessage("Welcome chatting sever~^^ ");
		session.sendMessage(textMessage);
		*/
		
		ChatMsgDTO chatMsgDTO = objectMapper.readValue(payload, ChatMsgDTO.class);
		ChatRoomDTO room = chatService.findRoomById(chatMsgDTO.getRoomId());
		room.handleActions(session, chatMsgDTO, chatService);
	}
}
