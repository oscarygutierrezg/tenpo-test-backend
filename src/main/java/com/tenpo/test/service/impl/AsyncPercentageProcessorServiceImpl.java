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

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class AsyncPercentageProcessorServiceImpl implements AsyncPercentageProcessorService {

    private final CalledHistoryService calledHistoryService;
    private final ObjectMapper objectMapper;
    private final HttpServletRequest httpServletRequest;

    @Override
    @Async
    public void saveHistory(Object response, Status status) {
        try {
            String responseJson = objectMapper.writeValueAsString(response);
            String url = Optional.ofNullable(httpServletRequest.getRequestURI()).orElse("N/A");
            CalledHistoryDto history = CalledHistoryDto.builder()
                    .response(responseJson)
                    .status(status)
                    .url(url)
                    .build();
            calledHistoryService.create(history);
        } catch (JsonProcessingException e) {
            log.error("Error serializando la respuesta para el historial: {}", e.getMessage(), e);
        }
    }
}