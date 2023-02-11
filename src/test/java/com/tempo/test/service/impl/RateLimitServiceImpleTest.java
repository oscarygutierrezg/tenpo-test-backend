package com.tempo.test.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tempo.test.dto.percentage.PercentageDto;
import com.tempo.test.reposiroty.StringRedisRepository;
import com.tempo.test.service.CalledHistoryService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class RateLimitServiceImpleTest {

	@InjectMocks
	private RateLimitServiceImpl rateLimitService;
	@Mock
	private ObjectMapper objectMapper;
	@Mock
	private  CalledHistoryService calledHistoryService;
	@Mock
	private  HttpServletRequest httpServletRequest;


	@SneakyThrows
	@Test
	void test_Validate_Should_AllowReqest_When_Invoked() {
		Bandwidth limit = Bandwidth.classic(1, Refill.greedy(1, Duration.ofMinutes(1)));
		var bucket= Bucket.builder()
				.addLimit(limit)
				.build();

		rateLimitService.validate(bucket);

		Assertions.assertEquals(0L,bucket.getAvailableTokens());
		Mockito.verify(objectMapper, Mockito.times(0)).writeValueAsString(Mockito.any());
		Mockito.verify(calledHistoryService, Mockito.times(0)).create(Mockito.any());
		Mockito.verify(httpServletRequest, Mockito.times(0)).getRequestURI();
	}

	@SneakyThrows
	@Test
	void test_Validate_Should_ForbidReqest_When_Invoked() {
		Mockito.when(objectMapper.writeValueAsString(Mockito.any(Object.class))).thenReturn("");
		Bandwidth limit = Bandwidth.classic(1, Refill.greedy(1, Duration.ofMinutes(1)));
		var bucket= Bucket.builder()
				.addLimit(limit)
				.build();
		rateLimitService.validate(bucket);

		assertThatThrownBy(() -> rateLimitService.validate(bucket))
				.isInstanceOf(ResponseStatusException.class)
				.hasMessage("429 TOO_MANY_REQUESTS \"Alcanzaste el máximo de requests permitidos por minuto, espera que pase un minuto y vuelve a intentar\"");

		Mockito.verify(calledHistoryService, Mockito.times(1)).create(Mockito.any());
		Mockito.verify(httpServletRequest, Mockito.times(1)).getRequestURI();
		Mockito.verify(objectMapper, Mockito.times(1)).writeValueAsString(Mockito.any());
	}

	@SneakyThrows
	@Test
	void test_Validate_Should_ForbidReqestAndErrorSaveHistory_When_Invoked() {
		Mockito.when(objectMapper.writeValueAsString(Mockito.any(Object.class))).thenThrow(new JsonProcessingException("Error"){});
		Bandwidth limit = Bandwidth.classic(1, Refill.greedy(1, Duration.ofMinutes(1)));
		var bucket= Bucket.builder()
				.addLimit(limit)
				.build();
		rateLimitService.validate(bucket);

		assertThatThrownBy(() -> rateLimitService.validate(bucket))
				.isInstanceOf(ResponseStatusException.class)
				.hasMessage("429 TOO_MANY_REQUESTS \"Alcanzaste el máximo de requests permitidos por minuto, espera que pase un minuto y vuelve a intentar\"");

		Mockito.verify(calledHistoryService, Mockito.times(0)).create(Mockito.any());
		Mockito.verify(httpServletRequest, Mockito.times(0)).getRequestURI();
		Mockito.verify(objectMapper, Mockito.times(1)).writeValueAsString(Mockito.any());
	}

}
