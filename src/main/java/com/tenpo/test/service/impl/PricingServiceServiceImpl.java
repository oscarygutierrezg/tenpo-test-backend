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
        String parameters = buildParameters(numOne, numTwo);
        PercentageDto percentage = getValidPercentage(parameters);
        PricingDto pricing = calculatePricing(percentage, numOne, numTwo);
        saveHistoryAsync(pricing, parameters, Status.SUCCESSFUL);
        return pricing;
    }

    private String buildParameters(long numOne, long numTwo) {
        return String.format("numOne=%d&numTwo=%d", numOne, numTwo);
    }

    private PercentageDto getValidPercentage(String parameters) {
        try {
            return Optional.ofNullable(percentageService.getCurrentCacheable())
                    .orElseThrow(() -> handlePercentageNotFound(parameters));
        } catch (Exception e) {
            log.error("Error conectando con redis: {}", e.getMessage(), e);
            return Optional.ofNullable(percentageService.getCurrent())
                    .orElseThrow(() -> handlePercentageNotFound(parameters));
        }
    }

    private ResponseStatusException handlePercentageNotFound(String parameters) {
        saveHistoryAsync(Constants.PRECONDITION_FAILED_MSG, parameters, Status.FAILED);
        return new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, Constants.PRECONDITION_FAILED_MSG);
    }

    private void saveHistoryAsync(Object response, String parameters, Status status) {
        CalledHistoryDto history = buildHistory(response, parameters, status);
        percentageProcessorService.saveHistory(history);
    }

    private CalledHistoryDto buildHistory(Object response, String parameters, Status status) {
        String responseJson = serializeResponse(response);
        return CalledHistoryDto.builder()
                .response(responseJson)
                .status(status)
                .parameters(parameters)
                .date(LocalDateTime.now())
                .endpoint(httpServletRequest.getRequestURI())
                .build();
    }

    private String serializeResponse(Object response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            log.error("Error serializando la respuesta para el historial: {}", e.getMessage(), e);
            return null;
        }
    }

    private PricingDto calculatePricing(PercentageDto percentage, long numOne, long numTwo) {
        double sum = numOne + numTwo;
        double result = sum + (sum * percentage.getValue());
        String formattedResult = new DecimalFormat(DECIMAL_FORMAT).format(result);
        return PricingDto.builder().result(formattedResult).build();
    }
}