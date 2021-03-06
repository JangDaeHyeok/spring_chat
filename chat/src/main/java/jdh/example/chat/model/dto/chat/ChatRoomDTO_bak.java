package jdh.example.chat.model.dto.chat;

//@Getter
//public class ChatRoomDTO_bak {
//	private String roomId;
//	private String name;
//	private Set<WebSocketSession> sessions = new HashSet<>();
//	
//	@Builder
//	public ChatRoomDTO_bak(String roomId, String name) {
//		this.roomId = roomId;
//		this.name = name;
//	}
//	
//	// 입장 시 Client 세션을 저장하고 입장 메시지 송신
//	public void handleActions(WebSocketSession session, ChatMsgDTO chatMsgDTO, ChatService chatService) {
//		if (chatMsgDTO.getType().equals(ChatMsgDTO.MessageType.ENTER)) {
//			sessions.add(session);
//			chatMsgDTO.setMessage(chatMsgDTO.getSender() + "님이 입장했습니다.");
//		}
//		sendMessage(chatMsgDTO, chatService);
//	}
//	
//	// 저장된 모든 session에 메시지 전달
//	public <T> void sendMessage(T message, ChatService chatService) {
//		sessions.parallelStream().forEach(session -> chatService.sendMessage(session, message));
//	}
//}
