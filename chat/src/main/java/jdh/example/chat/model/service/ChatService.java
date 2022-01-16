package jdh.example.chat.model.service;

/**
 * @author 장대혁
 * @date 2022-01-11
 * @description 채팅방 조회, 생성, 메시지 전송을 담당하는 service
 */
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class ChatService {
//	private final ObjectMapper objectMapper;
//	// 서버에 생성된 모든 채팅방 정보
//	private Map<String, ChatRoomDTO_bak> chatRooms;
//
//	@PostConstruct
//	private void init() {
//		chatRooms = new LinkedHashMap<>();
//	}
//	
//	// 모든 채팅방 정보 리턴
//	public List<ChatRoomDTO_bak> findAllRoom() {
//		return new ArrayList<>(chatRooms.values());
//	}
//	
//	// 채팅방 조회
//	public ChatRoomDTO_bak findRoomById(String roomId) {
//		return chatRooms.get(roomId);
//	}
//	
//	// 채팅방 생성
//	public ChatRoomDTO_bak createRoom(String name) {
//		String randomId = UUID.randomUUID().toString();
//		ChatRoomDTO_bak chatRoomDTO = ChatRoomDTO_bak.builder()
//				.roomId(randomId)
//				.name(name)
//				.build();
//		chatRooms.put(randomId, chatRoomDTO);
//		return chatRoomDTO;
//	}
//	
//	// 지정한 WebSocket session에 메시지 전송
//	public <T> void sendMessage(WebSocketSession session, T message) {
//		try {
//			session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
//		} catch (IOException e) {
//			log.error(e.getMessage(), e);
//		}
//	}
//}
