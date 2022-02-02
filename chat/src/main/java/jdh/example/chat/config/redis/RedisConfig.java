package jdh.example.chat.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import jdh.example.chat.redis.RedisSubscriber;

/**
 * @author 장대혁
 * @date 2021-01-15
 * @description MessageListner 설정 추가, redis 사용을 위한 redisTemplate 설정 추가
 */
@Configuration
public class RedisConfig {
	@Value("${spring.redis.host}")
	private String redisHost;
	@Value("${spring.redis.port}")
	private int redisPort;
//	@Value("${spring.redis.password}")
//	private String redisPassword;
	
	/* 
	 * 비밀번호가 없는 경우
	 */
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(redisHost, redisPort);
	}
	
	/*
	 * 비밀번호가 있는 경우
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(redisHost);
		redisStandaloneConfiguration.setPort(redisPort);
		redisStandaloneConfiguration.setPassword(redisPassword);
		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
		return lettuceConnectionFactory;
	}
	 */
	
	// 단일 Topic 사용을 위한 Bean 설정
	@Bean
	public ChannelTopic channelTopic() {
		return new ChannelTopic("jdh_chatroom");
	}
	
	// redis pub/sub 메시지를 처리하는 listner
	@Bean
	public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter, ChannelTopic channelTopic) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(listenerAdapter, channelTopic);
		return container;
	}
	
	// 실제 메시지를 처리하는 redis subscriber 설정 추가
	@Bean
	public MessageListenerAdapter listenerAdapter(RedisSubscriber subscriber) {
		// sendMessage 메소드를 실행하도록 어뎁터 설정
		return new MessageListenerAdapter(subscriber, "sendMessage");
	}
	
	// 어플리케이션에서 사용할 redisTemplate
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
		return redisTemplate;
	}
}
