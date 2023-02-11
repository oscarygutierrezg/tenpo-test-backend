package com.tempo.test.dto.history;

import com.tempo.test.model.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CalledHistoryDto{
	private String url;
	private String response;
	private Status status;
}
