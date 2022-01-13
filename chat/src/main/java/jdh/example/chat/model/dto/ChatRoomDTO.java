package jdh.example.chat.model.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomDTO {
	private String roomId;
	private String name;
	
	public static ChatRoomDTO create(String name) {
		ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
		chatRoomDTO.roomId = UUID.randomUUID().toString();
		chatRoomDTO.name = name;
		return chatRoomDTO;
	}
}
