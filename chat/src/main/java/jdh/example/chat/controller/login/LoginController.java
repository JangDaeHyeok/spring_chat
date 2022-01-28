package jdh.example.chat.controller.login;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jdh.example.chat.model.api.ApiResponseCode;
import jdh.example.chat.model.api.ApiResponseDTO;
import jdh.example.chat.model.api.ApiResponseResult;
import jdh.example.chat.model.dto.jwt.UserRefreshTokenTbDTO;
import jdh.example.chat.model.dto.user.UserLoginTbDTO;
import jdh.example.chat.model.dto.user.UserTbDTO;
import jdh.example.chat.model.service.jwt.UserRefreshTokenTbService;
import jdh.example.chat.model.service.user.UserLoginTbService;
import jdh.example.chat.model.service.user.UserTbService;
import jdh.example.chat.security.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Title       JWT 관련 발급 컨트롤러
 * @Author      장대혁
 * @Developer   장대혁
 * @Date        2022-01-28
 * @Description JWT 토큰 발급 컨트롤러, local storage 저장
 */
@RestController
public class LoginController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired UserTbService userTbService;
	@Autowired UserLoginTbService userLoginTbService;
	@Autowired UserRefreshTokenTbService userRefreshTokenTbService;
	@Autowired JwtTokenProvider jwtTokenUtil;
	
	// JWT 토큰 발급
	/* 
	 * localStorage 사용 시
	 */
	@PostMapping(value="login/authentication")
	public Map<String, Object> TestAdminAdminGet(@RequestBody Map<String, Object> input, HttpServletRequest req, HttpServletResponse rep) throws Exception{
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		UserLoginTbDTO uDTO = new UserLoginTbDTO();
		
		try {
			uDTO = userLoginTbService.getUserLoginTbOne(input.get("userId").toString(), input.get("userPw").toString());
		} catch (NotFoundException e) {
			returnMap = new ApiResponseDTO(ApiResponseResult.FAIL, ApiResponseCode.NOT_FOUND).getReturnMap();
			return returnMap;
		} catch (BadCredentialsException e) {
			returnMap = new ApiResponseDTO(ApiResponseResult.FAIL, ApiResponseCode.UNAUTHORIZED).getReturnMap();
			return returnMap;
		}
		
		// 권한 map 저장
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		Map<String, Object> rules = new HashMap<String, Object>();
		rules.put("rules", grantedAuthorities);
		
		// JWT 발급
		Map<String, String> tokens = jwtTokenUtil.generateTokenSet(uDTO.getUserId(), rules);
		String accessToken = URLEncoder.encode(tokens.get("accessToken"), "utf-8");
		String refreshToken = URLEncoder.encode(tokens.get("refreshToken"), "utf-8");
		
		// refresh token 정보 저장/수정
		UserRefreshTokenTbDTO rDTO = new UserRefreshTokenTbDTO();
		rDTO.setUserIdx(uDTO.getUserIdx());
		rDTO.setRefreshToken("Bearer " + refreshToken);
		userRefreshTokenTbService.addRefreshToken(rDTO);
		
		dataMap.put("accessToken", accessToken);
		returnMap = new ApiResponseDTO(ApiResponseResult.SUCEESS, ApiResponseCode.OK, dataMap).getReturnMap();
		
		return returnMap;
	}
	
	/*
	 *  JWT 토큰 발급(쿠키 사용 시)
	 
	@PostMapping(value="login/authentication")
	public Map<String, Object> testJwtTokenGet(@RequestBody Map<String, Object> input, HttpServletRequest req, HttpServletResponse rep) throws Exception{
		Map<String, Object> returnMap = new HashMap<String, Object>();
		UserLoginTbDTO uDTO = new UserLoginTbDTO();
		
		try {
			uDTO = userLoginTbService.getUserLoginTbOne(input.get("userId").toString(), input.get("userPw").toString());
		} catch (NotFoundException e) {
			returnMap = new ApiResponseDTO(ApiResponseResult.FAIL, ApiResponseCode.NOT_FOUND).getReturnMap();
			return returnMap;
		} catch (BadCredentialsException e) {
			returnMap = new ApiResponseDTO(ApiResponseResult.FAIL, ApiResponseCode.UNAUTHORIZED).getReturnMap();
			return returnMap;
		}
		
		// 권한 map 저장
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		Map<String, Object> rules = new HashMap<String, Object>();
		rules.put("rules", grantedAuthorities);
		
		// JWT 발급
		Map<String, String> tokens = jwtTokenUtil.generateTokenSet(uDTO.getUserId(), rules);
		
		String accessToken = URLEncoder.encode(tokens.get("accessToken"), "utf-8");
		String refreshToken = URLEncoder.encode(tokens.get("refreshToken"), "utf-8");
		
		log.info("[JWT 발급] accessToken : " + accessToken);
		log.info("[JWT 발급] refreshToken : " + refreshToken);
		
		// JWT 쿠키 저장(쿠키 명 : token)
		Cookie cookie = new Cookie("chatToken", "Bearer " + accessToken);
		cookie.setPath("/");
		cookie.setMaxAge(60 * 60 * 24 * 1); // 유효기간 1일
		// httoOnly 옵션을 추가해 서버만 쿠키에 접근할 수 있게 설정
		cookie.setHttpOnly(true);
		rep.addCookie(cookie);
		
		// 비밀번호 정보 제거
		uDTO.setUserPw("");
		
		// refresh token 정보 저장/수정
		UserRefreshTokenTbDTO rDTO = new UserRefreshTokenTbDTO();
		rDTO.setUserIdx(uDTO.getUserIdx());
		rDTO.setRefreshToken("Bearer " + refreshToken);
		userRefreshTokenTbService.addRefreshToken(rDTO);
		
		returnMap.put("result", "success");
		returnMap.put("msg", "JWT가 발급되었습니다.");
		return returnMap;
	}
	*/
	
	// JWT 토큰 재발급
	@PostMapping(value="login/refresh")
	public Map<String, Object> testJwtTokenRefresh(@RequestBody Map<String, Object> input, HttpServletRequest req, HttpServletResponse rep) throws Exception{
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String refreshToken = null;
		String userId = "";
		
		// 관리자 정보 조회
		UserTbDTO uDTO = userTbService.getUserTbOneById(input.get("userId").toString());
		
		// refreshToken 정보 조회
		UserRefreshTokenTbDTO rDTO = new UserRefreshTokenTbDTO();
		rDTO.setUserIdx(uDTO.getUserIdx());
		rDTO = userRefreshTokenTbService.getRefreshToken(rDTO);
		
		// token 정보가 존재하지 않는 경우
		if(rDTO == null) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "refresh token 정보가 존재하지 않습니다.");
			return returnMap;
		}
		// token 정보가 존재하는 경우
		else {
			refreshToken = rDTO.getRefreshToken();
		}
		
		// refreshToken 인증
		boolean tokenFl = false;
		try {
			refreshToken = refreshToken.substring(7);
			userId = jwtTokenUtil.getUsernameFromToken(refreshToken);
			tokenFl = true;
		} catch (SignatureException e) {
			log.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
		}
		
		// refreshToken 사용이 불가능한 경우
		if(!tokenFl) {
			returnMap.put("result", "fail");
			returnMap.put("msg", "refresh token이 만료되었거나 정보가 존재하지 않습니다.");
			
			// refreshToken 정보 조회 실패 시 기존에 존재하는 refreshToken 정보 삭제
			userRefreshTokenTbService.delRefreshToken(rDTO.getUserRefreshTokenIdx());
			
			return returnMap;
		}
		
		// refreshToken 인증 성공인 경우 accessToken 재발급
		if(userId != null && !userId.equals("")) {
			// 권한 map 저장
			List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			Map<String, Object> rules = new HashMap<String, Object>();
			rules.put("rules", grantedAuthorities);
			
			// JWT 발급
			String tokens = jwtTokenUtil.generateAccessToken(input.get("userId").toString(), rules);
			String accessToken = URLEncoder.encode(tokens, "utf-8");
			
			log.info("[JWT 재발급] accessToken : " + accessToken);
			
			// JWT 쿠키 저장(쿠키 명 : token)
			Cookie cookie = new Cookie("chatToken", "Bearer " + accessToken);
			cookie.setPath("/");
			cookie.setMaxAge(60 * 60 * 24 * 1); // 유효기간 1일
			// httoOnly 옵션을 추가해 서버만 쿠키에 접근할 수 있게 설정
			cookie.setHttpOnly(true);
			rep.addCookie(cookie);
			
			returnMap.put("result", "success");
			returnMap.put("msg", "JWT가 발급되었습니다.");
		}else {
			returnMap.put("result", "fail");
			returnMap.put("msg", "access token 발급 중 문제가 발생했습니다.");
			return returnMap;
		}
		
		return returnMap;
	}
	
	@Data
	class JwtRequest {
	private String email;
	private String password;
	}
	
	@Data
	@AllArgsConstructor
	class JwtResponse {
	private String token;
	}
}
