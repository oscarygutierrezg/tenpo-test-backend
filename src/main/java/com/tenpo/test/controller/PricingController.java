package com.tenpo.test.controller;

import com.tenpo.test.dto.pricing.PricingDto;
import com.tenpo.test.service.PricingService;
import com.tenpo.test.service.RateLimitService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "pricing")
@RestController
@RequestMapping(value = "/v1/pricing")
@Validated
public class PricingController {

	private final Bucket bucket;
	private  final PricingService pricingService;
	private final RateLimitService rateLimitService;

	public PricingController(Bandwidth limit, PricingService pricingService, RateLimitService rateLimitService) {
		this.pricingService = pricingService;
		this.rateLimitService = rateLimitService;
		bucket =  Bucket.builder()
				.addLimit(limit)
				.build();
	}


	@GetMapping(value = "/caculate/{numOne}/{numTwo}")
	public ResponseEntity<PricingDto>  caculate(
			@PathVariable(value = "numOne") @NotNull @Positive @Min(1) @Max(Long.MAX_VALUE-1) Long numOne,
			@PathVariable(value = "numTwo") @NotNull @Positive @Min(1) @Max(Long.MAX_VALUE-1) Long numTwo
	) {
		rateLimitService.validate(bucket);
		return ResponseEntity.ok().body(pricingService.getPrice(numOne,numTwo));
	}
}
