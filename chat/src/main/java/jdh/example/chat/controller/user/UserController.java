package jdh.example.chat.controller.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.javassist.bytecode.DuplicateMemberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jdh.example.chat.model.api.ApiResponseCode;
import jdh.example.chat.model.api.ApiResponseDTO;
import jdh.example.chat.model.api.ApiResponseResult;
import jdh.example.chat.model.dto.user.UserRegistTbDTO;
import jdh.example.chat.model.dto.user.UserTbDTO;
import jdh.example.chat.model.service.user.UserRegistTbService;
import jdh.example.chat.model.service.user.UserTbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
	@Autowired UserTbService userTbService;
	@Autowired UserRegistTbService userRegistTbService;
	
	@GetMapping(value="user")
	public Map<String, Object> userListGet() throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		UserTbDTO userTbDTO = new UserTbDTO();
		userTbDTO.setDelYn("N");
		List<UserTbDTO> list = userTbService.getUserTbList(userTbDTO);
		dataMap.put("list", list);
		
		log.info("사용자 정보 목록 조회");
		
		returnMap = new ApiResponseDTO(dataMap).getReturnMap();
		return returnMap;
	}
	
	@GetMapping(value="user/{userIdx}")
	public Map<String, Object> userOneGetByIdx(@PathVariable String userIdx) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		try {
			UserTbDTO one = userTbService.getUserTbOneByIdx(Integer.parseInt(userIdx));
			dataMap.put("one", one);
		} catch (NumberFormatException e) {
			returnMap = new ApiResponseDTO(ApiResponseResult.FAIL, ApiResponseCode.BAD_PARAMETER).getReturnMap();
			return returnMap;
		} catch (Exception e) {
			returnMap = new ApiResponseDTO(ApiResponseResult.FAIL, ApiResponseCode.SERVER_ERROR).getReturnMap();
			return returnMap;
		}
		
		log.info("사용자 정보 조회");
		
		returnMap = new ApiResponseDTO(dataMap).getReturnMap();
		return returnMap;
	}
	
	// 사용자 정보 등록
	@PostMapping(value="user/join")
	public Map<String, Object> userOneGetById(@RequestBody UserRegistTbDTO userRegistTbDTO) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		log.info("사용자 정보 등록");
		
		try {
			userRegistTbService.addUserTb(userRegistTbDTO);
		} catch (DuplicateMemberException e) {
			returnMap = new ApiResponseDTO(ApiResponseResult.FAIL, ApiResponseCode.DUPLICATE_MEMBER).getReturnMap();
			return returnMap;
		}
		
		returnMap = new ApiResponseDTO(ApiResponseResult.SUCEESS, ApiResponseCode.OK).getReturnMap();
		return returnMap;
	}
	
	@PatchMapping(value="user")
	public Map<String, Object> userEdit(@RequestBody UserRegistTbDTO userRegistTbDTO) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		try {
			userRegistTbService.editUserTb(userRegistTbDTO);
		} catch (DuplicateMemberException e) {
			returnMap = new ApiResponseDTO(ApiResponseResult.FAIL, ApiResponseCode.DUPLICATE_MEMBER).getReturnMap();
			return returnMap;
		}
		
		returnMap = new ApiResponseDTO(ApiResponseResult.SUCEESS, ApiResponseCode.OK).getReturnMap();
		return returnMap;
	}
}
