package jdh.example.chat.model.repository;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import jdh.example.chat.model.dto.chat.ChatRoomDTO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {
	private static final String CHAT_ROOMS = "CHAT_ROOM";
	public static final String USER_COUNT = "USER_COUNT";
	public static final String ENTER_INFO = "ENTER_INFO";
	
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, ChatRoomDTO> opsHashChatRoom;
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, String> hashOpsEnterInfo;
	@Resource(name = "redisTemplate")
	private ValueOperations<String, String> valueOps;
	
	// 모든 채팅방 조회
	public List<ChatRoomDTO> findAllRoom() {
		return opsHashChatRoom.values(CHAT_ROOMS);
	}
	// 특정 채팅방 조회
	public ChatRoomDTO findRoomById(String id) {
		return opsHashChatRoom.get(CHAT_ROOMS, id);
	}
	// 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장
	public ChatRoomDTO createChatRoom(String name) {
		ChatRoomDTO chatRoomDTO = ChatRoomDTO.create(name);
		opsHashChatRoom.put(CHAT_ROOMS, chatRoomDTO.getRoomId(), chatRoomDTO);
		return chatRoomDTO;
	}
	
	// 유저가 입장한 채팅방ID와 유저 세션ID 맵핑 정보 저장
	public void setUserEnterInfo(String sessionId, String roomId) {
		hashOpsEnterInfo.put(ENTER_INFO, sessionId, roomId);
	}
	
	// 유저 세션으로 입장해 있는 채팅방 ID 조회
	public String getUserEnterRoomId(String sessionId) {
		return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
	}
	
	// 유저 세션정보와 맵핑된 채팅방ID 삭제
	public void removeUserEnterInfo(String sessionId) {
		hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
	}
	
	// 채팅방 유저수 조회
	public long getUserCount(String roomId) {
		return Long.valueOf(Optional.ofNullable(valueOps.get(USER_COUNT + "_" + roomId)).orElse("0"));
	}
	
	// 채팅방에 입장한 유저수 +1
	public long plusUserCount(String roomId) {
		return Optional.ofNullable(valueOps.increment(USER_COUNT + "_" + roomId)).orElse(0L);
	}
	
	// 채팅방에 입장한 유저수 -1
	public long minusUserCount(String roomId) {
		return Optional.ofNullable(valueOps.decrement(USER_COUNT + "_" + roomId)).filter(count -> count > 0).orElse(0L);
	}
}
