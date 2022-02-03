package jdh.example.chat.security.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jdh.example.chat.model.dto.user.UserTbDTO;
import jdh.example.chat.model.service.user.UserTbService;

/**
 * @Title       JWT Request Filter
 * @Author      장대혁
 * @Developer   장대혁
 * @Date        2022-01-28
 * @Description JWT을 검증하는 filter
 *              OncePerRequestFilter를 상속해 요청당 한번의 filter를 수행한다.
 *              EXCLUDE_URL에 있는 url은 체크하지 않는다.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private UserTbService service;
	@Autowired private JwtTokenProvider jwtTokenProvider;
	
	// 포함하지 않을 url
	private static final List<String> EXCLUDE_URL =
		Collections.unmodifiableList(
			Arrays.asList(
				"/static/**",
				"/favicon.ico",
				"/user/join",
				"/login/authentication",
				"/login/refresh",
				"/jdh-stomp"
			));
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// jwt local storage 사용 시
		final String token = request.getHeader("Authorization");
		
		// jwt cookie 사용 시
		/*
		String token = Arrays.stream(request.getCookies())
				.filter(c -> c.getName().equals("chatToken"))
				.findFirst() .map(Cookie::getValue)
				.orElse(null);
		*/
		
		String userId = null;
		String jwtToken = null;
		
		// Bearer token인 경우
		log.info("[JwtRequestFilter] token :: {}", token);
		if (token != null && token.startsWith("Bearer ")) {
			jwtToken = token.substring(7);
			try {
				userId = jwtTokenProvider.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				log.error("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				log.error("JWT Token has expired");
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}
		
		// token 검증이 되고 인증 정보가 존재하지 않는 경우 spring security 인증 정보 저장
		if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserTbDTO userTbDTO = new UserTbDTO();
			try {
				userTbDTO = service.getUserTbOneById(userId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(jwtTokenProvider.validateToken(jwtToken)) {
				List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
				
				UsernamePasswordAuthenticationToken authenticationToken =
						new UsernamePasswordAuthenticationToken(userTbDTO, null, grantedAuthorities);
				
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}
		
		// accessToken 인증이 되었다면 refreshToken 재발급이 필요한 경우 재발급
		try {
			if(userId != null) {
				jwtTokenProvider.reGenerateRefreshToken(userId);
			}
		}catch (Exception e) {
			log.error("[JwtRequestFilter] refreshToken 재발급 체크 중 문제 발생 : {}", e.getMessage());
		}
		
		filterChain.doFilter(request, response);
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return EXCLUDE_URL.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
	}
}
