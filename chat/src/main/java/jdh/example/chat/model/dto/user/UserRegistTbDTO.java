package jdh.example.chat.model.dto.user;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistTbDTO {
	@NotEmpty
	private String userId;
	
	private String salt;
	
	@NotEmpty
	private String userPw;
	
	@NotEmpty
	private String nickname;
	
	private String logo;
}
