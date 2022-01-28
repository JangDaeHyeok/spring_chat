package jdh.example.chat.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.security.access.SecurityConfig;

import jdh.example.chat.ApiCommonTest;
import jdh.example.chat.controller.user.UserController;
import jdh.example.chat.model.dto.user.UserRegistTbDTO;

@WebMvcTest(controllers = UserController.class
			, excludeFilters = { //!Added!
					@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class) })
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest extends ApiCommonTest {
	@Test
	@DisplayName("사용자 목록 조회")
	void testUserListGet() throws Exception {
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/user").param("userIdx", "1").param("userId", "test").param("delYn", "N"))
		.andExpect(status().isOk())
		.andDo(document("userList",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestParameters(
						parameterWithName("userIdx").description("사용자 IDX").optional(),
						parameterWithName("userId").description("사용자 ID").optional(),
						parameterWithName("delYn").description("삭제여부").optional().attributes(Attributes.key("format").value("Y/N"))
					),
				PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("result").type(JsonFieldType.STRING)
						.description("결과"),
						PayloadDocumentation.fieldWithPath("msg").type(JsonFieldType.STRING)
						.description("메시지"),
						PayloadDocumentation.fieldWithPath("data.list").type(JsonFieldType.ARRAY)
						.description("사용자 목록").optional()
						)
				)
			);
	}
	
	@Test
	@DisplayName("사용자 정보 단건 조회")
	void testUserOneGet() throws Exception {
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/user/1"))
		.andExpect(status().isOk())
		.andDo(document("userOne",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(parameterWithName("userIdx").description("사용자 IDX").optional().attributes(Attributes.key("format").value("숫자"))),
				PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("result").type(JsonFieldType.STRING)
						.description("결과"),
						PayloadDocumentation.fieldWithPath("msg").type(JsonFieldType.STRING)
						.description("메시지"),
						PayloadDocumentation.fieldWithPath("data.one").type(JsonFieldType.STRING)
						.description("사용자 정보").optional()
						)
				)
			);
	}
	
	@Test
	@DisplayName("사용자 등록(회원가입)")
	void testUserAdd() throws Exception {
		UserRegistTbDTO userRegistTbDTO = new UserRegistTbDTO();
		userRegistTbDTO.setUserId("test");
		userRegistTbDTO.setNickname("테스트");
		userRegistTbDTO.setUserPw("1234");
		
		String body = objectMapper.writeValueAsString(userRegistTbDTO);
		
		// get validation
		ConstraintDescriptions userRegistTbDTOConstraints = new ConstraintDescriptions(UserRegistTbDTO.class);
		
		mockMvc.perform(
				post("/user/join").content(body).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andDo(document("userAdd",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("userId").type(JsonFieldType.STRING)
							.description("사용자 ID").attributes(key("constraint").value(userRegistTbDTOConstraints.descriptionsForProperty("userId"))),
						PayloadDocumentation.fieldWithPath("userPw").type(JsonFieldType.STRING)
							.description("사용자 비밀번호").attributes(key("constraint").value(userRegistTbDTOConstraints.descriptionsForProperty("userPw"))),
						PayloadDocumentation.fieldWithPath("nickname").type(JsonFieldType.STRING)
							.description("닉네임").attributes(key("constraint").value(userRegistTbDTOConstraints.descriptionsForProperty("nickname"))),
						PayloadDocumentation.fieldWithPath("salt").type(JsonFieldType.STRING)
						.description("비밀번호 암호화 난수").attributes(key("constraint").value(userRegistTbDTOConstraints.descriptionsForProperty("salt"))).optional()
						),
				PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("result").type(JsonFieldType.STRING)
						.description("결과"),
						PayloadDocumentation.fieldWithPath("msg").type(JsonFieldType.STRING)
						.description("메시지")
						)
				)
				);
	}
}
