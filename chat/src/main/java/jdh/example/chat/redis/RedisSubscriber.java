package jdh.example.chat.redis;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jdh.example.chat.model.dto.chat.ChatMsgDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 장대혁
 * @date 2022-01-15
 * @description 메시ㅣ지 리스너에 메시지가 수신되면 websocket 클라이언트에 메시지 전송
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {
	private final ObjectMapper objectMapper;
	private final SimpMessageSendingOperations messagingTemplate;
	/**
	 * Redis에서 메시지가 발행(publish)되면 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리한다.
	 */
	public void sendMessage(String publishMessage) {
		log.info("[RedisSubscriber] 수신 메시지 :: " + publishMessage);
		try {
			// ChatMessage 객채로 맵핑
			ChatMsgDTO chatMessage = objectMapper.readValue(publishMessage, ChatMsgDTO.class);
			// 채팅방을 구독한 클라이언트에게 메시지 발송
			messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
		} catch (Exception e) {
			log.error("Exception {}", e);
		}
	}
}
