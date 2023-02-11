package com.tenpo.test.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.test.constants.Constants;
import com.tenpo.test.dto.history.CalledHistoryDto;
import com.tenpo.test.model.enums.Status;
import com.tenpo.test.service.CalledHistoryService;
import com.tenpo.test.service.RateLimitService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
@Slf4j
public class RateLimitServiceImpl implements RateLimitService {

    private final CalledHistoryService calledHistoryService;
    private final ObjectMapper objectMapper;
    private final HttpServletRequest httpServletRequest;


    @Async
    public void saveHistory(Object response, Status status) {
        try{
            calledHistoryService.create(CalledHistoryDto.builder()
                    .response(objectMapper.writeValueAsString(response))
                    .status(status)
                    .url(httpServletRequest.getRequestURI())
                    .build());
        } catch (Exception e){
            log.error(e.getMessage(),e);
        }

    }

    @Override
    public void validate(Bucket bucket) {
        if (!bucket.tryConsume(1)) {
            saveHistory(Constants.TOO_MANY_REQUESTS_MSG,Status.FAILED);
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, Constants.TOO_MANY_REQUESTS_MSG , null);
        }
    }
}
