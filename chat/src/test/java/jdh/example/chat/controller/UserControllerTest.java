package jdh.example.chat.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import jdh.example.chat.controller.user.UserController;
import jdh.example.chat.model.service.UserTbService;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(UserController.class)
@AutoConfigureRestDocs
public class UserControllerTest {
	@Autowired MockMvc mockMvc;
	
	@Autowired ObjectMapper objectMapper;
	
	@MockBean private UserTbService service;
	@MockBean private DataSource dataSource;
	
	@Test
	@DisplayName("사용자 목록 조회")
	void testHello4() throws Exception {
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/user").param("userIdx", "1").param("userId", "test").param("delYn", "N"))
		.andExpect(status().isOk())
		.andDo(document("userList",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestParameters(
						parameterWithName("userIdx").description("사용자 IDX"),
						parameterWithName("userId").description("사용자 ID"),
						parameterWithName("delYn").description("삭제여부")
					),
				PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("result").type(JsonFieldType.STRING)
						.description("결과"),
						PayloadDocumentation.fieldWithPath("msg").type(JsonFieldType.STRING)
						.description("메시지"),
						PayloadDocumentation.fieldWithPath("data.list").type(JsonFieldType.ARRAY)
						.description("사용자 목록")
						)
				)
			);
	}
}
