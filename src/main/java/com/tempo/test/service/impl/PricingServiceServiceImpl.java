package com.tempo.test.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tempo.test.constants.Constants;
import com.tempo.test.dto.history.CalledHistoryDto;
import com.tempo.test.dto.percentage.PercentageDto;
import com.tempo.test.dto.pricing.PricingDto;
import com.tempo.test.model.enums.Status;
import com.tempo.test.reposiroty.StringRedisRepository;
import com.tempo.test.service.CalledHistoryService;
import com.tempo.test.service.PricingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.text.DecimalFormat;


@Service
@AllArgsConstructor
@Slf4j
public class PricingServiceServiceImpl implements PricingService {

    private final CalledHistoryService calledHistoryService;
    private final ObjectMapper objectMapper;
    private final HttpServletRequest httpServletRequest;
    private final StringRedisRepository stringRedisRepository;

    @Override
    public PricingDto getPrice(long numOne, long numTwo) {
        var percentage = findPercentage();
        var pricing = calculatePrice(percentage, numOne, numTwo);

        saveHistory(pricing,Status.SUCCESSFUL);
        return pricing;
    }

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

    @SneakyThrows
    private PercentageDto findPercentage() {
        if(stringRedisRepository.getBy(Constants.CURRENT_PERCENTAGE_VALUE)==null){
            saveHistory(Constants.PRECONDITION_FAILED_MSG,Status.FAILED);
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, Constants.PRECONDITION_FAILED_MSG , null);
        }
        return objectMapper.readValue(stringRedisRepository.getBy(Constants.CURRENT_PERCENTAGE_VALUE), PercentageDto.class);
    }

    private PricingDto calculatePrice(PercentageDto percentage, long numOne, long numTwo) {
        var sum = numOne+numTwo;
        var result = sum+(sum*percentage.getValue());
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(2);
        return  PricingDto.builder().result(df.format(result)).build();
    }
}
