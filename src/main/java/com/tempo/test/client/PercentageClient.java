package com.tempo.test.client;

import com.tempo.test.dto.percentage.PercentageDto;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "percentageClient", url = "${app.ms.percentage.url}")
public interface PercentageClient {
	
	@GetMapping(
			value = "/v1/percentage/current",
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	@Retry(name = "percentageClient", fallbackMethod = "getCurrentOrdersFallbackForRetry")
	PercentageDto getCurrent();

	default PercentageDto getCurrentOrdersFallbackForRetry(Exception e) {
		return null;
	}
	
	

}
