package com.tenpo.test.client;

import com.tenpo.test.dto.percentage.PercentageDto;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "percentageClient", url = "${app.ms.percentage.url}", fallback = PercentageClientFallback.class)
public interface PercentageClient {
	
	@GetMapping(
			value = "/v1/percentage/current",
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	@Retry(name = "percentageClient", fallbackMethod = "getCurrentPercentageFallbackForRetry")
	PercentageDto getCurrent();

	default PercentageDto getCurrentPercentageFallbackForRetry(Exception e) {
		return null;
	}
}
