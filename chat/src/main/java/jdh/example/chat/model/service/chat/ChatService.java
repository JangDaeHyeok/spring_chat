package jdh.example.chat.model.service.chat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import jdh.example.chat.model.dto.chat.ChatMsgDTO;
import jdh.example.chat.model.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author 장대혁
 * @date 2022-01-11
 * @description 채팅방 조회, 생성, 메시지 전송을 담당하는 service
 */
@RequiredArgsConstructor
@Service
public class ChatService {
	private final ChannelTopic channelTopic;
	private final RedisTemplate<String, Object> redisTemplate;
	private final ChatRoomRepository chatRoomRepository;
	
	public String getRoomId(String destination) {
		int lastIndex = destination.lastIndexOf('/');
		if (lastIndex != -1)
			return destination.substring(lastIndex + 1);
		else
			return "";
	}
	
	// 메시지 발송
	public void sendChatMessage(ChatMsgDTO chatMsgDTO) {
		chatMsgDTO.setUserCount(chatRoomRepository.getUserCount(chatMsgDTO.getRoomId()));
		if (ChatMsgDTO.MessageType.ENTER.equals(chatMsgDTO.getType())) {
			chatMsgDTO.setMessage(chatMsgDTO.getSender() + "님이 방에 입장했습니다.");
			chatMsgDTO.setSender("[알림]");
		} else if (ChatMsgDTO.MessageType.QUIT.equals(chatMsgDTO.getType())) {
			chatMsgDTO.setMessage(chatMsgDTO.getSender() + "님이 방에서 나갔습니다.");
			chatMsgDTO.setSender("[알림]");
		} else {
			// 채팅 전송시간
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			chatMsgDTO.setRegDt(now.format(formatter));
		}
		
		redisTemplate.convertAndSend(channelTopic.getTopic(), chatMsgDTO);
	}
}
