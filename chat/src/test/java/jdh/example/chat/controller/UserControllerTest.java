package jdh.example.chat.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.snippet.Attributes.key;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import jdh.example.chat.controller.user.UserController;
import jdh.example.chat.model.dto.UserTbDTO;
import jdh.example.chat.model.service.UserTbService;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(UserController.class)
@AutoConfigureRestDocs
public class UserControllerTest {
	@Autowired MockMvc mockMvc;
	
	@Autowired ObjectMapper objectMapper;
	
	@MockBean private UserTbService service; // WebMvcTest에서 service annotation 이용을 위한 MockBean 추가
	@MockBean private DataSource dataSource; // db 이용 시 mockbean 추가
	
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
		UserTbDTO userTbDTO = new UserTbDTO();
		userTbDTO.setUserId("test");
		userTbDTO.setNickname("테스트");
		userTbDTO.setUserPw("1234");
		userTbDTO.setDelYn("N");
		
		String body = objectMapper.writeValueAsString(userTbDTO);
		
		// get validation
		ConstraintDescriptions userTbDTOConstraints = new ConstraintDescriptions(UserTbDTO.class);
		
		mockMvc.perform(
				post("/user").content(body).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andDo(document("userAdd",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				PayloadDocumentation.requestFields(
						PayloadDocumentation.fieldWithPath("userIdx").type(JsonFieldType.NUMBER)
							.description("사용자 IDX").optional(),
						PayloadDocumentation.fieldWithPath("userId").type(JsonFieldType.STRING)
							.description("사용자 ID").attributes(key("constraint").value(userTbDTOConstraints.descriptionsForProperty("userId"))),
						PayloadDocumentation.fieldWithPath("salt").type(JsonFieldType.STRING)
							.description("사용자 비밀번호 암호화 난수값").optional(),
						PayloadDocumentation.fieldWithPath("userPw").type(JsonFieldType.STRING)
							.description("사용자 비밀번호").attributes(key("constraint").value(userTbDTOConstraints.descriptionsForProperty("userPw"))),
						PayloadDocumentation.fieldWithPath("nickname").type(JsonFieldType.STRING)
							.description("닉네임").attributes(key("constraint").value(userTbDTOConstraints.descriptionsForProperty("nickname"))),
						PayloadDocumentation.fieldWithPath("regDt").type(JsonFieldType.STRING)
							.description("등록일자").optional(),
							PayloadDocumentation.fieldWithPath("modDt").type(JsonFieldType.STRING)
							.description("등록일자").optional(),
						PayloadDocumentation.fieldWithPath("delYn").type(JsonFieldType.STRING)
							.description("삭제여부").optional()
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
