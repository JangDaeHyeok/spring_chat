package jdh.example.chat.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomDTO implements Serializable {
	// redis에 저장되는 객체는 직렬화가 가능해야 함으로 직렬화 세팅
	private static final long serialVersionUID = 6494678977089006639L;

	private String roomId;
	private String name;
	private String regDt;
	
	public static ChatRoomDTO create(String name) {
		ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
		chatRoomDTO.roomId = UUID.randomUUID().toString();
		chatRoomDTO.name = name;
		
		// 채팅방 생성일자
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		chatRoomDTO.regDt = now.format(formatter);
		
		return chatRoomDTO;
	}
}