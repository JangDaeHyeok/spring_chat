package jdh.example.chat.model.dto.user;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTbDTO {
	private int userIdx;
	
	@NotEmpty
	private String userId;
	
	@NotEmpty
	private String nickname;
	
	private String logo;
	
	private String regDt;
	
	private String modDt;
	
	private String delYn;
}
