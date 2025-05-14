package com.tenpo.test.dto.history;

import com.tenpo.test.model.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CalledHistoryDto{
	private String parameters;
	private String endpoint;
	private LocalDateTime date;
	private String response;
	private Status status;
}
