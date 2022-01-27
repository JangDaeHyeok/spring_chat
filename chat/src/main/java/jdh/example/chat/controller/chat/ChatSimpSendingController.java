package jdh.example.chat.controller.chat;

/**
 * @author 장대혁
 * @date 2022-01-14
 * @description stomp 메시지 전송 컨트롤러
 *              Message Mapping을 /chat/message로 설정하고
 *              클라이언트에서 /pub/chat/message로 발행 요청
 *              /sub/chat/room/roomId 해당 주소를 구독(subscribe)하고 있고, 메시지 전달
 */
// @RequiredArgsConstructor
// @Controller
//public class ChatSimpSendingController {
//	private final SimpMessageSendingOperations messagingTemplate;
//	
//	@MessageMapping("/chat/message")
//	public void message(ChatMsgDTO message) {
//		if (ChatMsgDTO.MessageType.ENTER.equals(message.getType()))
//			message.setMessage(message.getSender() + "님이 입장하셨습니다.");
//		
//		messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
//	}
//}
