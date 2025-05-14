package com.tenpo.test.service;


import com.tenpo.test.dto.history.CalledHistoryDto;
import com.tenpo.test.model.enums.Status;

public interface AsyncPercentageProcessorService {

	void saveHistory(CalledHistoryDto history);
}
