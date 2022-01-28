package jdh.example.chat.model.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import jdh.example.chat.model.dto.chat.ChatRoomDTO;
import jdh.example.chat.redis.RedisSubscriber;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {
	// 채팅방(topic) 발행 메시지 처리 Listener
	private final RedisMessageListenerContainer redisMessageListener;
	
	// 구독 처리 서비스
	private final RedisSubscriber redisSubscriber;
	
	// redis
	private static final String CHAT_ROOMS = "CHAT_ROOM";
	private final RedisTemplate<String, Object> redisTemplate;
	private HashOperations<String, String, ChatRoomDTO> opsHashChatRoom;
	
	/*
	 * 채팅방 대화 메시지를 발행하기 위한 redis topic 정보
	 * 서버별로 채팅방에 매치되는 topic 정보를 Map에 넣어 roomId로 찾을 수 있도록 함
	 */
	private Map<String, ChannelTopic> topics;
	
	@PostConstruct
	private void init() {
		opsHashChatRoom = redisTemplate.opsForHash();
		topics = new HashMap<String, ChannelTopic>();
	}
	
	public List<ChatRoomDTO> findAllRoom() {
		return opsHashChatRoom.values(CHAT_ROOMS);
	}
	
	public ChatRoomDTO findRoomById(String id) {
		return opsHashChatRoom.get(CHAT_ROOMS, id);
	}
	
	// 채팅방 생성 후 redis hash 저장
	public ChatRoomDTO createChatRoom(String name) {
		ChatRoomDTO chatRoomDTO = ChatRoomDTO.create(name);
		opsHashChatRoom.put(CHAT_ROOMS, chatRoomDTO.getRoomId(), chatRoomDTO);
		return chatRoomDTO;
	}
	
	// 채팅방 입장 후 topic 생성, pub/sub 통신을 위한 리스너 설정
	public void enterChatRoom(String roomId) {
		ChannelTopic topic = topics.get(roomId);
		if(topic == null) {
			topic = new ChannelTopic(roomId);
			redisMessageListener.addMessageListener(redisSubscriber, topic);
			topics.put(roomId, topic);
		}
	}
	
	public ChannelTopic getTopic(String roomId) {
		return topics.get(roomId);
	}
}
