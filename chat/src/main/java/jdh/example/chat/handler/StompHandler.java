package jdh.example.chat.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import jdh.example.chat.security.jwt.JwtTokenProvider;

@Component
public class StompHandler implements ChannelInterceptor {
	@Autowired JwtTokenProvider jwtTokenProvider;
	
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
			
		}
		// 채팅
		else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
			
		}
		
		return message;
	}
}
