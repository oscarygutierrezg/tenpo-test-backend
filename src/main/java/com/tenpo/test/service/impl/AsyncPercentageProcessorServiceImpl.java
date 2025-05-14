package com.tenpo.test.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.test.dto.history.CalledHistoryDto;
import com.tenpo.test.model.enums.Status;
import com.tenpo.test.service.AsyncPercentageProcessorService;
import com.tenpo.test.service.CalledHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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