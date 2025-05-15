package com.tenpo.test.service;

import com.tenpo.test.dto.history.CalledHistoryDto;
import com.tenpo.test.model.CalledHistory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CalledHistoryService {

	void create(CalledHistoryDto calledHistoryRequest) ;

	Page<CalledHistoryDto> index(Example<CalledHistory> example, Pageable pageable);

}
