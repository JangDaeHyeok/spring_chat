package jdh.example.chat.model.dto.chat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMsgDTO implements Serializable {
	// redis에 저장되는 객체는 직렬화가 가능해야 함으로 직렬화 세팅
	private static final long serialVersionUID = 6494678977089007639L;
	
	public ChatMsgDTO() {
		
	}
	
	@Builder
	public ChatMsgDTO(MessageType type, String roomId, String sender, String message, long userCount) {
		this.type = type;
		this.roomId = roomId;
		this.sender = sender;
		this.message = message;
		this.userCount = userCount;
		
		// 채팅 전송시간
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		this.regDt = now.format(formatter);
	}
	
	// 메시지 타입 : 입장, 채팅
	public enum MessageType {
		ENTER, QUIT, TALK
	}
	
	private MessageType type; // 메시지 타입
	private String roomId; // 방번호
	private String sender; // 메시지 보낸사람
	private String message; // 메시지
	private long userCount; // 채팅방 입장인원 수
	private String regDt;
}
