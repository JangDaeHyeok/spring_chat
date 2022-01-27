package jdh.example.chat.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTbDTO {
	private int userIdx;
	private String userId;
	private String nickname;
	private String regDt;
	private String modDt;
	private String delYn;
}
