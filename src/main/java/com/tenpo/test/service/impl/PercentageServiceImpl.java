package com.tenpo.test.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.test.client.PercentageClient;
import com.tenpo.test.constants.Constants;
import com.tenpo.test.reposiroty.StringRedisRepository;
import com.tenpo.test.service.PercentageService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PercentageServiceImpl implements PercentageService {

    private final PercentageClient percentageClient;
    private final StringRedisRepository stringRedisRepository;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    @EventListener(ApplicationReadyEvent.class)
    public void saveCurrent() {
        var percentage = percentageClient.getCurrent();
        if(percentage != null){
            stringRedisRepository.add(Constants.CURRENT_PERCENTAGE_VALUE, objectMapper.writeValueAsString(percentage));
        }
    }
}
