package jdh.example.chat.model.dto;

import java.io.Serializable;
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
	
	public static ChatRoomDTO create(String name) {
		ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
		chatRoomDTO.roomId = UUID.randomUUID().toString();
		chatRoomDTO.name = name;
		return chatRoomDTO;
	}
}