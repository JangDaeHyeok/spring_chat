package jdh.example.chat.config.redis;

// import redis.embedded.RedisServer;

/**
 * @author 장대혁
 * @date 2022-01-15
 * @description 로컬 환경일 경우 내장 레디스를 실행하는 config
 */
//@Profile("local")
//@Configuration
//public class EmbeddedRedisConfig {
//	@Value("${spring.redis.port}")
//	private int redisPort;
//	
//	private RedisServer redisServer;
//	
//	@PostConstruct
//	public void redisServer() {
//		redisServer = new RedisServer(redisPort);
//		redisServer.start();
//	}
//	
//	@PreDestroy
//	public void stopRedisServer() {
//		if(redisServer != null) {
//			redisServer.stop();
//		}
//	}
//}
