package jdh.example.chat.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import jdh.example.chat.model.dto.ChatMsgDTO;
import lombok.RequiredArgsConstructor;

/**
 * @author 장대혁
 * @date 2022-01-15
 * @description 주제(topic/채팅방)에 대하여 구독자(subscriber)에게 메시지를 발행(publish)하는 redis 발생 서비스
 */
@RequiredArgsConstructor
@Service
public class RedisPublisher {
	private final RedisTemplate<String, Object> redisTemplate;
	
	public void publish(ChannelTopic topic, ChatMsgDTO chatMsgDTO) {
		redisTemplate.convertAndSend(topic.getTopic(), chatMsgDTO);
	}
}
