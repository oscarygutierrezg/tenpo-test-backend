package com.tenpo.test.service.impl;

import com.tenpo.test.dto.history.CalledHistoryDto;
import com.tenpo.test.service.AsyncPercentageProcessorService;
import com.tenpo.test.service.CalledHistoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AsyncPercentageProcessorServiceImpl implements AsyncPercentageProcessorService {

    private final CalledHistoryService calledHistoryService;


    @Override
    @Async
    public void saveHistory(CalledHistoryDto history) {
        calledHistoryService.create(history);
    }
}