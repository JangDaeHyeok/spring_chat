package jdh.example.chat.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jdh.example.chat.security.handler.JwtAuthenticationEntryPoint;
import jdh.example.chat.security.jwt.JwtRequestFilter;

/**
 * @Title       Spring Security JWT Config
 * @Author      장대혁
 * @Developer   장대혁
 * @Date        2022-01-28
 * @Description Spring Security JWT Config
 */
@Configuration
@EnableWebSecurity
public class ChatSecurityConfig extends WebSecurityConfigurerAdapter {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	@Autowired private JwtRequestFilter jwtRequestFilter;
	
	// 정적 자원에 대해서는 Security 설정을 적용하지 않음
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.error("*********************************************************************");
		log.error("[WebSecurityConfig] Spring Security 설정");
		
		http.csrf().disable()
			.headers().frameOptions().sameOrigin()
			.and()
			.authorizeRequests()
			// 사용자 로그인 spring security 적용 제외 항목
			.antMatchers("/chat/**").hasRole("USER")
			.anyRequest().permitAll()
			// jwt가 없는 경우 exception handler 설정
			.and()
			.exceptionHandling()
			.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			// jwt filter 적용
			.and()
			.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		
		log.error("[WebSecurityConfig] Spring Security 설정 완료");
		log.error("*********************************************************************");
	}
}
