package com.tenpo.test.service.impl;

import com.tenpo.test.dto.history.CalledHistoryDto;
import com.tenpo.test.dto.history.CalledHistoryMapper;
import com.tenpo.test.model.CalledHistory;
import com.tenpo.test.reposiroty.CalledHistoryRepository;
import com.tenpo.test.service.CalledHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CalledHistoryServiceImpl implements CalledHistoryService {

    private final CalledHistoryRepository calledHistoryRepository;
    private final CalledHistoryMapper calledHistoryMapper;

    @Override
    public void create(CalledHistoryDto calledHistoryRequest) {
        calledHistoryRepository.save(calledHistoryMapper.toModel(calledHistoryRequest));
    }

    @Override
    public Page<CalledHistoryDto> index(Example<CalledHistory> example, Pageable pageable) {
        return calledHistoryRepository.findAll(example,pageable).map(calledHistoryMapper::toDto);
    }
}
