package com.tenpo.test.controller;

import com.tenpo.test.dto.pricing.PricingDto;
import com.tenpo.test.service.PricingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class PricingController {

	private  final PricingService pricingService;

	@GetMapping(value = "/caculate/{numOne}/{numTwo}")
	public ResponseEntity<PricingDto>  caculate(
			@PathVariable(value = "numOne") @NotNull @Positive @Min(1) @Max(Long.MAX_VALUE-1) Long numOne,
			@PathVariable(value = "numTwo") @NotNull @Positive @Min(1) @Max(Long.MAX_VALUE-1) Long numTwo
	) {
		return ResponseEntity.ok().body(pricingService.getPrice(numOne,numTwo));
	}
}
