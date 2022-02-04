package jdh.example.chat.handler;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import jdh.example.chat.model.dto.chat.ChatMsgDTO;
import jdh.example.chat.model.repository.ChatRoomRepository;
import jdh.example.chat.model.service.chat.ChatService;
import jdh.example.chat.security.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StompHandler implements ChannelInterceptor {
	@Autowired JwtTokenProvider jwtTokenProvider;
	@Autowired ChatRoomRepository chatRoomRepository;
	@Autowired ChatService chatService;
	
	// websocket을 통해 들어온 요청이 처리 되기전 실행
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		// websocket 연결 시
		if (StompCommand.CONNECT == accessor.getCommand()) {
			// 헤더의 jwt token 검증
			jwtTokenProvider.validateToken(accessor.getFirstNativeHeader("Authorization"));
		}
		// 채팅방 구독 요청 시
		else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
			log.info("[StompHandler] header :: " + message.getHeaders());
			
			// header정보에서 구독 채팅방 정보를 얻고, roomId를 추출
			String roomId = chatService.getRoomId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));
			// 채팅방에 들어온 클라이언트 sessionId를 roomId와 맵핑 (나중에 특정 세션이 어떤 채팅방에 들어가 있는지 알기 위함)
			String sessionId = (String) message.getHeaders().get("simpSessionId");
			chatRoomRepository.setUserEnterInfo(sessionId, roomId);
			// 채팅방 인원수 +1
			chatRoomRepository.plusUserCount(roomId);
			
			// 채팅 전송시간
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			
			// 클라이언트 입장 메시지를 채팅방에 발송 (redis publish)
			String name = Optional.ofNullable((Principal) message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");
			String nickname = name.contains(":::") ? name.split(":::")[1] : name;
			chatService.sendChatMessage(ChatMsgDTO.builder().type(ChatMsgDTO.MessageType.ENTER).roomId(roomId).sender(nickname).regDt(now.format(formatter)).build());
			log.info("[StompHandler] subscribe {}, {}", name, roomId);
		}
		// websocket 연결 종료 시
		else if (StompCommand.DISCONNECT == accessor.getCommand()) {
			log.info("[StompHandler] header :: " + message.getHeaders());
			
			// 연결이 종료된 클라이언트 sesssionId로 채팅방 id를 조회
			String sessionId = (String) message.getHeaders().get("simpSessionId");
			String roomId = chatRoomRepository.getUserEnterRoomId(sessionId);
			// 채팅방 인원수 -1
			chatRoomRepository.minusUserCount(roomId);
			
			// 채팅 전송시간
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			
			// 클라이언트 퇴장 메시지를 채팅방에 발송 (redis publish)
			String name = Optional.ofNullable((Principal) message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");
			String nickname = name.contains(":::") ? name.split(":::")[1] : name;
			chatService.sendChatMessage(ChatMsgDTO.builder().type(ChatMsgDTO.MessageType.QUIT).roomId(roomId).sender(nickname).regDt(now.format(formatter)).build());
			// 퇴장한 클라이언트의 roomId 맵핑 정보 삭제
			chatRoomRepository.removeUserEnterInfo(sessionId);
			log.info("[StompHandler] disconnect {}, {}", sessionId, roomId);
		}
		
		return message;
	}
}
