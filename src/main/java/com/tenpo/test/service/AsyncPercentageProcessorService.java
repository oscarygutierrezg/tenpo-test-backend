package com.tenpo.test.service;

import com.tenpo.test.dto.history.CalledHistoryDto;

public interface AsyncPercentageProcessorService {

	void saveHistory(CalledHistoryDto history);
}
