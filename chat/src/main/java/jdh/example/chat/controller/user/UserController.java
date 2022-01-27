package jdh.example.chat.controller.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jdh.example.chat.model.api.ApiResponseCode;
import jdh.example.chat.model.api.ApiResponseDTO;
import jdh.example.chat.model.api.ApiResponseResult;
import jdh.example.chat.model.dto.UserTbDTO;
import jdh.example.chat.model.service.UserTbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
	@Autowired UserTbService userTbService;
	
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
	
	@GetMapping(value="user/{userId}")
	public Map<String, Object> userOneGetById(@PathVariable String userId) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		UserTbDTO one = userTbService.getUserTbOne(userId);
		dataMap.put("one", one);
		
		log.info("사용자 정보 조회");
		
		returnMap = new ApiResponseDTO(dataMap).getReturnMap();
		return returnMap;
	}
	
	@PostMapping(value="user")
	public Map<String, Object> userOneGetById(@RequestBody UserTbDTO userTbDTO) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		userTbService.addUserTb(userTbDTO);
		
		log.info("사용자 정보 등록");
		
		returnMap = new ApiResponseDTO(ApiResponseResult.SUCEESS, ApiResponseCode.OK).getReturnMap();
		return returnMap;
	}
}
