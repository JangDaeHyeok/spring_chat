package jdh.example.chat;

import javax.sql.DataSource;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import jdh.example.chat.model.service.jwt.UserRefreshTokenTbService;
import jdh.example.chat.model.service.user.UserLoginTbService;
import jdh.example.chat.model.service.user.UserRegistTbService;
import jdh.example.chat.model.service.user.UserTbService;
import jdh.example.chat.security.handler.JwtAuthenticationEntryPoint;
import jdh.example.chat.security.jwt.JwtTokenProvider;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs
public abstract class ApiCommonTest {
	@Autowired protected MockMvc mockMvc;
	
	@Autowired protected ObjectMapper objectMapper;
	
	// WebMvcTest에서 service annotation 이용을 위한 MockBean 추가
	@MockBean protected UserTbService service; 
	@MockBean protected UserLoginTbService userLoginTbService; 
	@MockBean protected UserRegistTbService userRegistTbService;
	@MockBean protected UserRefreshTokenTbService userRefreshTokenTbService;
	
	// WebMvcTest에서 component annotation 이용을 위한 MockBean 추가
	@MockBean protected JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	@MockBean protected JwtTokenProvider jwtTokenProvider;
	
	@MockBean protected DataSource dataSource; // db 이용 시 mockbean 추가
}
