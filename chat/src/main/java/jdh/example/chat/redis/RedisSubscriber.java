package jdh.example.chat.redis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jdh.example.chat.model.dto.chat.ChatMsgDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 장대혁
 * @date 2022-01-15
 * @description 
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {
	private final ObjectMapper objectMapper;
	private final RedisTemplate<String, Object> redisTemplate;
	private final SimpMessageSendingOperations messagingTemplate;
	
	// redis에서 메시지가 발생(publish)되면 대기하고 있던 onMessage가 해당 메시지를 수신 후 처리
	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			// redis에서 발행한 데이터 deserialize
			String publishMessage = new String(redisTemplate.getStringSerializer().deserialize(message.getBody()));
			log.info("[onMessage] 메시지 publish" + publishMessage);
			
			// ChatMessage 객체로 매핑
			ChatMsgDTO chatMsgDTO = objectMapper.readValue(publishMessage, ChatMsgDTO.class);
			
			// 채팅 전송시간
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			chatMsgDTO.setRegDt(now.format(formatter));
			
			// WebSocket 구독자에게 채팅 메시지 전송
			messagingTemplate.convertAndSend("/sub/chat/room/" + chatMsgDTO.getRoomId(), chatMsgDTO);
		} catch (Exception e) {
			log.error("[RedisSubscriber] redis 구독 중 문제 발생 : {}", e.getMessage());
		}
	}

}
