package jdh.example.chat.model.service.chat;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jdh.example.chat.model.dao.chat.ChatMsgTbMapper;
import jdh.example.chat.model.dto.chat.ChatMsgDTO;
import jdh.example.chat.model.dto.chat.ChatRoomDTO;
import jdh.example.chat.model.repository.ChatMsgRepository;
import jdh.example.chat.model.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 장대혁
 * @date 2022-01-11
 * @description 채팅방 조회, 생성, 메시지 전송을 담당하는 service
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMsgService {
	private final int ADD_CHAT_MSG_DELAY = 1000 * 60 * 10; // 10분
	
	private final ChatRoomRepository chatRoomRepository;
	private final ChatMsgRepository chatMsgRepository;
	
	@Autowired ChatMsgTbMapper chatMsgTbMapper;
	
	public List<ChatMsgDTO> getChatMsgList(Map<String, Object> input) throws Exception {
		return chatMsgTbMapper.selectChatMsgTbList(input);
	}
	
	// redis 채팅 임시저장 내역 조히 후 mysql에 저장, redis 채팅 임시저장 내역 초기화
	@Transactional
	@Scheduled(fixedRate = ADD_CHAT_MSG_DELAY)
	public void addChatMsg() throws Exception {
		log.info("[addChatMsg] chat redis to mysql ==> Start");
		// 모든 채팅방 내역 조회
		List<ChatRoomDTO> roomList = chatRoomRepository.findAllRoom();
		
		for(ChatRoomDTO room : roomList) {
			// redis 채팅 임시저장 내역 조회
			List<ChatMsgDTO> chatList = chatMsgRepository.getChatMsgList(room.getRoomId());
			
			for(ChatMsgDTO chat : chatList) {
				// log.info(":::>> {} :: {} :: {}", chat.getRoomId(), chat.getMessage(), chat.getType());
				// redis to mysql insert
				chatMsgTbMapper.insetChatMsgTb(chat);
			}
			// redis 채팅 임시저장 내역 초기화
			chatMsgRepository.removeChatMsg(room.getRoomId());
		}
		log.info("[addChatMsg] chat redis to mysql ==> End");
	}
}
