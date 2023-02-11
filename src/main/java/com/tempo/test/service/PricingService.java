package com.tempo.test.service;

import com.tempo.test.dto.pricing.PricingDto;


public interface PricingService {

	PricingDto getPrice(long numOne, long numTwo);
}
