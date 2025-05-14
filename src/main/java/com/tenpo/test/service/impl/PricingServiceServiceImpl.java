package com.tenpo.test.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.test.constants.Constants;
import com.tenpo.test.dto.history.CalledHistoryDto;
import com.tenpo.test.dto.percentage.PercentageDto;
import com.tenpo.test.dto.pricing.PricingDto;
import com.tenpo.test.model.enums.Status;
import com.tenpo.test.service.AsyncPercentageProcessorService;
import com.tenpo.test.service.PercentageService;
import com.tenpo.test.service.PricingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class PricingServiceServiceImpl implements PricingService {

    private static final String DECIMAL_FORMAT = "#.##";

    private final AsyncPercentageProcessorService percentageProcessorService;
    private final PercentageService percentageService;
    private final ObjectMapper objectMapper;
    private final HttpServletRequest httpServletRequest;


    @Override
    public PricingDto getPrice(long numOne, long numTwo) {
        String parameters = String.format("numOne=%d&numTwo=%d", numOne, numTwo);
        PercentageDto percentage = Optional.ofNullable(percentageService.getCurrent())
                .orElseGet(() -> {
                    var history = createHistory(Constants.PRECONDITION_FAILED_MSG, parameters, Status.FAILED);
                    percentageProcessorService.saveHistory(history);
                    throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, Constants.PRECONDITION_FAILED_MSG);
                });

        PricingDto pricing = calculatePrice(percentage, numOne, numTwo);
        var history = createHistory(pricing, parameters ,Status.SUCCESSFUL);
        percentageProcessorService.saveHistory(history);
        return pricing;
    }

    private CalledHistoryDto createHistory(Object response, String parameters, Status status) {
        String responseJson = null;
        try {
            responseJson = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            log.error("Error serializando la respuesta para el historial: {}", e.getMessage(), e);
        }
        return CalledHistoryDto.builder()
                .response(responseJson)
                .status(status)
                .parameters(parameters)
                .date(LocalDateTime.now())
                .endpoint(httpServletRequest.getRequestURI())
                .build();

    }

    private PricingDto calculatePrice(PercentageDto percentage, long numOne, long numTwo) {
        double sum = (numOne + numTwo);
        double result = sum + (sum * percentage.getValue());
        DecimalFormat df = new DecimalFormat(DECIMAL_FORMAT);
        return PricingDto.builder().result(df.format(result)).build();
    }
}