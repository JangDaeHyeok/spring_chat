package jdh.example.chat.model.dto.user;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistTbDTO {
	@NotEmpty
	private String userId;
	
	@NotEmpty
	private String nickname;
	
	@NotEmpty
	private String userPw;
	
	private String salt;
}
