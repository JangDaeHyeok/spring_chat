package jdh.example.chat.model.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTbDTO {
	private int userIdx;
	
	@NotEmpty
	private String userId;
	
	private String salt;
	
	@NotEmpty
	private String userPw;
	
	@NotEmpty
	private String nickname;
	
	private String regDt;
	
	private String modDt;
	
	private String delYn;
}
