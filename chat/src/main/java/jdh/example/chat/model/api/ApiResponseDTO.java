package jdh.example.chat.model.api;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class ApiResponseDTO {
	private Map<String, Object> returnMap = new HashMap<String, Object>();
	private String message;
	private String result;
	
	public ApiResponseDTO(Map<String, Object> input) {
		this.bindResult(ApiResponseResult.SUCEESS);
		this.bindCode(ApiResponseCode.OK);
		
		returnMap.put("data", input);
	}
	
	public ApiResponseDTO(ApiResponseResult result, ApiResponseCode status) {
		this.bindResult(result);
		this.bindCode(status);
	}
	
	public ApiResponseDTO(ApiResponseResult result, ApiResponseCode status, Map<String, Object> input) {
		this.bindResult(result);
		this.bindCode(status);
		
		if(input.containsKey("list"))
			returnMap.put("list", input);
		else if(input.containsKey("one"))
			returnMap.put("one", input);
	}
	
	private void bindResult(ApiResponseResult result) { 
		this.result = result.getText();
		returnMap.put("result", this.result);
	}
	
	private void bindCode(ApiResponseCode status) { 
		this.message = status.getText();
		returnMap.put("msg", this.message);
	}
}
