package jdh.example.chat.config.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import jdh.example.chat.handler.StompHandler;
import jdh.example.chat.security.jwt.JwtTokenProvider;

/**
 * 
 * @author 장대혁
 * @date 2022-01-11
 * @description WebSocket을 활성화하고 Stomp WebSocket 접속 엔드포인트를 /jdh-stomp으로 설정
 *			    Stomp를 이용한 pub/sub 방식을 사용하기 위해 메시지 브로거 prefix 설정
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Autowired JwtTokenProvider jwtTokenProvider;
	@Autowired StompHandler stompHandler;
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		// 메시지 구독 요청 prefix 설정
		config.enableSimpleBroker("/sub");
		// 메시지 발행 요청 prefix 설정
		config.setApplicationDestinationPrefixes("/pub");
	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// WebSocket endpoint 설정
		registry.addEndpoint("/jdh-stomp").setAllowedOriginPatterns("*");
	}
	
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(stompHandler);
	}
}
