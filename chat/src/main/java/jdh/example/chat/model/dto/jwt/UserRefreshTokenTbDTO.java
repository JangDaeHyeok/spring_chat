package jdh.example.chat.model.dto.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRefreshTokenTbDTO {
	private int userRefreshTokenIdx = 0;
	private int userIdx             = 0;
	private String refreshToken     = null;
}
