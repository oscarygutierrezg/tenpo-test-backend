package com.tenpo.test.service.impl;

import com.tenpo.test.constants.Constants;
import com.tenpo.test.dto.percentage.PercentageDto;
import com.tenpo.test.dto.pricing.PricingDto;
import com.tenpo.test.model.enums.Status;
import com.tenpo.test.service.AsyncPercentageProcessorService;
import com.tenpo.test.service.PercentageService;
import com.tenpo.test.service.PricingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.DecimalFormat;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class PricingServiceServiceImpl implements PricingService {

    private static final String DECIMAL_FORMAT = "#.##";

    private final AsyncPercentageProcessorService percentageProcessorService;
    private final PercentageService percentageService;

    @Override
    public PricingDto getPrice(long numOne, long numTwo) {
        PercentageDto percentage = Optional.ofNullable(percentageService.getCurrent())
                .orElseGet(() -> {
                    percentageProcessorService.saveHistory(Constants.PRECONDITION_FAILED_MSG, Status.FAILED);
                    throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, Constants.PRECONDITION_FAILED_MSG);
                });

        PricingDto pricing = calculatePrice(percentage, numOne, numTwo);
        percentageProcessorService.saveHistory(pricing, Status.SUCCESSFUL);
        return pricing;
    }

    private PricingDto calculatePrice(PercentageDto percentage, long numOne, long numTwo) {
        double sum = (numOne + numTwo);
        double result = sum + (sum * percentage.getValue());
        DecimalFormat df = new DecimalFormat(DECIMAL_FORMAT);
        return PricingDto.builder().result(df.format(result)).build();
    }
}