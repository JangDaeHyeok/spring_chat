package jdh.example.chat.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jdh.example.chat.model.dto.ChatRoomDTO_bak;
import jdh.example.chat.model.service.ChatService;
import jdh.example.chat.util.ValidateUtil;
import lombok.RequiredArgsConstructor;

/**
 * @author 장대혁
 * @date 2022-01-11
 * @description 채팅방 생성 및 조회 RestAPI 구현
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {
	@Autowired ChatService chatService;
	
	// 채팅방 생성
	@PostMapping
	public ChatRoomDTO_bak createRoom(@RequestBody Map<String, Object> input) {
		String name = "test";
		if(ValidateUtil.checkNotEmpty(input.get("name"))) name = input.get("name").toString();
		
		return chatService.createRoom(name);
	}
	
	// 채팅방 조회
	@GetMapping
	public List<ChatRoomDTO_bak> findAllRoom() {
		return chatService.findAllRoom();
	}
}
