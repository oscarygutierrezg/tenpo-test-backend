package com.tenpo.test.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.test.dto.percentage.PercentageDto;
import com.tenpo.test.reposiroty.StringRedisRepository;
import com.tenpo.test.service.CalledHistoryService;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class PricingServiceServiceImplTest {

	@InjectMocks
	private PricingServiceServiceImpl pricingServiceService;
	@Mock
	private StringRedisRepository stringRedisRepository;
	@Mock
	private ObjectMapper objectMapper;
	@Mock
	private  CalledHistoryService calledHistoryService;
	@Mock
	private  HttpServletRequest httpServletRequest;


	@SneakyThrows
	@Test
	void test_GetPrice_Should_ReturnPrice_When_Invoked() {
		var percentageDto = new PercentageDto();
		percentageDto.setValue(1D);
		Mockito.when(stringRedisRepository.getBy(Mockito.anyString())).thenReturn("");
		Mockito.when(objectMapper.readValue(Mockito.anyString(), Mockito.eq(PercentageDto.class))).thenReturn(percentageDto);

		var price = pricingServiceService.getPrice(1,2);

		Assertions.assertNotNull(price);
		Assertions.assertNotNull(price.getResult());
		Assertions.assertEquals("6",price.getResult());
		Mockito.verify(objectMapper, Mockito.times(1)).writeValueAsString(Mockito.any());
		Mockito.verify(objectMapper, Mockito.times(1)).readValue(Mockito.anyString(), Mockito.eq(PercentageDto.class));
		Mockito.verify(calledHistoryService, Mockito.times(1)).create(Mockito.any());
		Mockito.verify(httpServletRequest, Mockito.times(1)).getRequestURI();
		Mockito.verify(stringRedisRepository, Mockito.times(2)).getBy(Mockito.anyString());
	}

	@SneakyThrows
	@Test
	void test_GetPrice_Should_ReturnResponseStatusException_When_Invoked() {
		Mockito.when(stringRedisRepository.getBy(Mockito.anyString())).thenReturn(null);

		assertThatThrownBy(() -> pricingServiceService.getPrice(1,2))
				.isInstanceOf(ResponseStatusException.class)
				.hasMessage("412 PRECONDITION_FAILED \"No se pudo encontrar el porcenjate\"");


		Mockito.verify(objectMapper, Mockito.times(1)).writeValueAsString(Mockito.any());
		Mockito.verify(objectMapper, Mockito.times(0)).readValue(Mockito.anyString(), Mockito.eq(PercentageDto.class));
		Mockito.verify(calledHistoryService, Mockito.times(1)).create(Mockito.any());
		Mockito.verify(httpServletRequest, Mockito.times(1)).getRequestURI();
		Mockito.verify(stringRedisRepository, Mockito.times(1)).getBy(Mockito.anyString());
	}

	@SneakyThrows
	@Test
	void test_GetPrice_Should_ReturnPriceAndErrorSaveHistory_When_Invoked() {
		var percentageDto = new PercentageDto();
		percentageDto.setValue(1D);
		Mockito.when(stringRedisRepository.getBy(Mockito.anyString())).thenReturn("");
		Mockito.when(objectMapper.readValue(Mockito.anyString(), Mockito.eq(PercentageDto.class))).thenReturn(percentageDto);
		Mockito.when(objectMapper.writeValueAsString(Mockito.any(Object.class))).thenThrow(new JsonProcessingException("Error"){});


		var price = pricingServiceService.getPrice(1,2);

		Assertions.assertNotNull(price);
		Assertions.assertNotNull(price.getResult());
		Assertions.assertEquals("6",price.getResult());
		Mockito.verify(objectMapper, Mockito.times(1)).writeValueAsString(Mockito.any());
		Mockito.verify(objectMapper, Mockito.times(1)).readValue(Mockito.anyString(), Mockito.eq(PercentageDto.class));
		Mockito.verify(calledHistoryService, Mockito.times(0)).create(Mockito.any());
		Mockito.verify(httpServletRequest, Mockito.times(0)).getRequestURI();
		Mockito.verify(stringRedisRepository, Mockito.times(2)).getBy(Mockito.anyString());
	}

}
