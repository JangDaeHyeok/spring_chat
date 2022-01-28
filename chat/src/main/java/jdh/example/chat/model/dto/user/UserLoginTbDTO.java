package jdh.example.chat.model.dto.user;

import java.util.Collection;

import javax.validation.constraints.NotEmpty;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginTbDTO {
	private int userIdx;
	
	@NotEmpty
	private String userId;
	
	private String salt;
	
	@NotEmpty
	private String userPw;
	
	// jwt token
	private String token                   = null;
	
	Collection<? extends GrantedAuthority> authorities;
}
