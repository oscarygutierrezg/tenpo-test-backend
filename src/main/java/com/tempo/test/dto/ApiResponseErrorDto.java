package com.tempo.test.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ApiResponseErrorDto {
	
	private int code;
	private String message;
	private List<String> errors;

}

