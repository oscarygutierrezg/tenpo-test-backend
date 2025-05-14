package com.tenpo.test.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.test.dto.history.CalledHistoryDto;
import com.tenpo.test.dto.percentage.PercentageDto;
import com.tenpo.test.service.AsyncPercentageProcessorService;
import com.tenpo.test.service.CalledHistoryService;
import com.tenpo.test.service.PercentageService;
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
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class PricingServiceServiceImplTest {

	@InjectMocks
	private PricingServiceServiceImpl pricingServiceService;
	@Mock
	private PercentageService percentageService;
	@Mock
	private AsyncPercentageProcessorService percentageProcessorService;

	@SneakyThrows
	@Test
	void test_GetPrice_Should_ReturnPrice_When_Invoked() {
		var percentageDto = new PercentageDto();
		percentageDto.setValue(1D);
		Mockito.when(percentageService.getCurrent()).thenReturn(percentageDto);

		var price = pricingServiceService.getPrice(1,2);

		Assertions.assertNotNull(price);
		Assertions.assertNotNull(price.getResult());
		Assertions.assertEquals("6",price.getResult());
		Mockito.verify(percentageService, Mockito.times(1)).getCurrent();
		Mockito.verify(percentageProcessorService, Mockito.times(1)).saveHistory(Mockito.any(), Mockito.any());
	}

	@SneakyThrows
	@Test
	void test_GetPrice_Should_ReturnResponseStatusException_When_Invoked() {
		Mockito.when(percentageService.getCurrent()).thenReturn(null);

		assertThatThrownBy(() -> pricingServiceService.getPrice(1,2))
				.isInstanceOf(ResponseStatusException.class)
				.hasMessage("412 PRECONDITION_FAILED \"No se pudo encontrar el porcenjate\"");


		Mockito.verify(percentageService, Mockito.times(1)).getCurrent();
		Mockito.verify(percentageProcessorService, Mockito.times(1)).saveHistory(Mockito.any(), Mockito.any());
	}
}
