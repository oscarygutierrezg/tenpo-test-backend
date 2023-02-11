package com.tempo.test.service;

import com.tempo.test.dto.history.CalledHistoryDto;
import com.tempo.test.model.CalledHistory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CalledHistoryService {

	void create(CalledHistoryDto calledHistoryRequest);

	Page<CalledHistoryDto> index(Example<CalledHistory> example, Pageable pageable);

}
