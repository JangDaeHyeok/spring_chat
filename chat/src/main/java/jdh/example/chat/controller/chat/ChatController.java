package jdh.example.chat.controller.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jdh.example.chat.model.api.ApiResponseCode;
import jdh.example.chat.model.api.ApiResponseDTO;
import jdh.example.chat.model.api.ApiResponseResult;
import jdh.example.chat.model.dto.chat.ChatMsgDTO;
import jdh.example.chat.model.repository.ChatMsgRepository;
import jdh.example.chat.model.repository.ChatRoomRepository;
import jdh.example.chat.model.service.chat.ChatMsgService;
import jdh.example.chat.model.service.chat.ChatService;
import jdh.example.chat.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 장대혁
 * @date 2022-01-15
 * @description 메시지 pub으로 들어오는 경우 처리하는 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {
	@Autowired JwtTokenProvider jwtTokenProvider;
	@Autowired ChatRoomRepository chatRoomRepository;
	@Autowired ChatMsgRepository chatMsgRepository;
	@Autowired ChatService chatService;
	@Autowired ChatMsgService chatMsgService;
	
	// WebSocket /pub/chat/message 로 들어오는 메시지 처리
	@MessageMapping("/chat/message")
	public void message(ChatMsgDTO chatMsgDTO, @Header("Authorization") String token) throws Exception {
		// JWT 토큰 정보를 이용한 message sender 설정
		chatMsgDTO.setSender(jwtTokenProvider.getNicknameFromToken(token.substring(7)));
		
		// 채팅방 인원수 세팅
		chatMsgDTO.setUserCount(chatRoomRepository.getUserCount(chatMsgDTO.getRoomId()));
		
		// Websocket에 발행된 메시지를 redis로 발행(publish)
		try {
			chatService.sendChatMessage(chatMsgDTO);
		} catch (NullPointerException e) {
			log.error("[ChatController] 채팅 전송 중 문제 발생 : ", e.getMessage());
		}
	}
	
	// 채팅내역 조회
	@GetMapping("/chat/{roomId}")
	@ResponseBody
	public Map<String, Object> chatMsgGet(@PathVariable String roomId
			, @RequestParam(required = false, defaultValue = "1") String pageNo, @RequestParam(required = false, defaultValue = "20") String row) throws Exception {
		// return Map
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// mybatis parameter DTO
		Map<String, Object> inputMap = new HashMap<String, Object>();
		// return data Map
		Map<String, Object> dataMap = new HashMap<String, Object>();
		// chat redis List
		List<ChatMsgDTO> chatRedisList = new ArrayList<ChatMsgDTO>();
		// chat List
		List<ChatMsgDTO> chatList = new ArrayList<ChatMsgDTO>();
		
		// s :: set parameter and select :: /
		try {
			int thisRow = Integer.parseInt(row) > 50 ? 50 : Integer.parseInt(row);
			int thisPageNo = Integer.parseInt(pageNo) < 1 ? 1 : (Integer.parseInt(pageNo) - 1) * thisRow;
			
			inputMap.put("roomId", roomId);
			// row 수 50 제한
			inputMap.put("row", thisRow);
			// pageNo 1 밑으로 못내려가게 제한
			inputMap.put("pageNo", thisPageNo);
			
			// 가장 최근내역 조회 시 redis 임시저장 내역과 함께 조회
			if(Integer.parseInt(pageNo) == 1) {
				chatRedisList = chatMsgRepository.getChatMsgList(roomId);
			}
			// 채팅내역 조회
			chatList = chatMsgService.getChatMsgList(inputMap);
			// 채팅 임시저장 내역 + 이전 채팅내역 합치기
			chatRedisList.addAll(chatList);
		} catch (NumberFormatException e) {
			returnMap = new ApiResponseDTO(ApiResponseResult.FAIL, ApiResponseCode.BAD_PARAMETER).getReturnMap();
			return returnMap;
		} catch (Exception e) {
			returnMap = new ApiResponseDTO(ApiResponseResult.FAIL, ApiResponseCode.SERVER_ERROR).getReturnMap();
			return returnMap;
		}
		// e :: set parameter and select :: //
		
		dataMap.put("list", chatRedisList);
		returnMap = new ApiResponseDTO(ApiResponseResult.SUCEESS, ApiResponseCode.OK, dataMap).getReturnMap();
		return returnMap;
	}
}
