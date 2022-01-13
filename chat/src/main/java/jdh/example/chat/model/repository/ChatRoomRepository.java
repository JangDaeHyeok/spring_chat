package jdh.example.chat.model.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import jdh.example.chat.model.dto.ChatRoomDTO;

@Repository
public class ChatRoomRepository {
	private Map<String, ChatRoomDTO> chatRoomMap;
	
	// PostContruct를 사용해 호출 전 init을 자동으로 호출해 초기화
	@PostConstruct
	private void init() {
		chatRoomMap = new LinkedHashMap<>();
	}
	
	public List<ChatRoomDTO> findAllRoom() {
		// 채팅방 생성순서 최근 순으로 반환
		List chatRooms = new ArrayList<>(chatRoomMap.values());
		Collections.reverse(chatRooms);
		return chatRooms;
	}
	
	public ChatRoomDTO findRoomById(String id) {
		return chatRoomMap.get(id);
	}
	
	public ChatRoomDTO createChatRoom(String name) {
		ChatRoomDTO chatRoomDTO = ChatRoomDTO.create(name);
		chatRoomMap.put(chatRoomDTO.getRoomId(), chatRoomDTO);
		return chatRoomDTO;
	}
}
