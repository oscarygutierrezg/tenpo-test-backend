package com.tenpo.test.service;

import com.tenpo.test.dto.pricing.PricingDto;


public interface PricingService {

	PricingDto getPrice(long numOne, long numTwo);
}
