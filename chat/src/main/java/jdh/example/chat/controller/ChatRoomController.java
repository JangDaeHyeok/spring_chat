package jdh.example.chat.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jdh.example.chat.model.dto.ChatRoomDTO;
import jdh.example.chat.model.repository.ChatRoomRepository_bak;
import lombok.RequiredArgsConstructor;

/**
 * @author 장대혁
 * @date 2022-01-14
 * @description 채팅방 관련 컨트롤러
 *              view와 api 통신을 위해 ResponseBody 어노테이션 설정
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {
	private final ChatRoomRepository_bak chatRoomRepository;
	
	// 모든 채팅방 목록 반환
	@GetMapping("/rooms")
	@ResponseBody
	public List<ChatRoomDTO> roomListGet() {
		return chatRoomRepository.findAllRoom();
	}
	
	// 채팅방 생성
	@PostMapping("/room")
	@ResponseBody
	public ChatRoomDTO createRoom(@RequestParam String name) {
		return chatRoomRepository.createChatRoom(name);
	}
	
	// 특정 채팅방 조회
	@GetMapping("/room/{roomId}")
	@ResponseBody
	public ChatRoomDTO roomDetail(@PathVariable String roomId) {
		return chatRoomRepository.findRoomById(roomId);
	}
}
