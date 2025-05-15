package com.tenpo.test.unit.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.test.dto.percentage.PercentageDto;
import com.tenpo.test.service.AsyncPercentageProcessorService;
import com.tenpo.test.service.PercentageService;
import com.tenpo.test.service.impl.PricingServiceServiceImpl;
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
class PricingServiceServiceImplUnitTest {

	@InjectMocks
	private PricingServiceServiceImpl pricingServiceService;
	@Mock
	private PercentageService percentageService;
	@Mock
	private AsyncPercentageProcessorService percentageProcessorService;
	@Mock
	private ObjectMapper objectMapper;
	@Mock
	private HttpServletRequest httpServletRequest;

	@SneakyThrows
	@Test
	void givenValidInput_whenGetPrice_thenReturnsCalculatedPrice() {
		// Arrange
		var percentageDto = new PercentageDto();
		percentageDto.setValue(1D);
		Mockito.when(percentageService.getCurrent()).thenReturn(percentageDto);

		// Act
		var price = pricingServiceService.getPrice(1, 2);

		// Assert
		Assertions.assertNotNull(price);
		Assertions.assertNotNull(price.getResult());
		Assertions.assertEquals("6", price.getResult());
		Mockito.verify(percentageService, Mockito.times(1)).getCurrent();
		Mockito.verify(percentageProcessorService, Mockito.times(1)).saveHistory(Mockito.any());
		Mockito.verify(objectMapper, Mockito.times(1)).writeValueAsString(Mockito.any());
		Mockito.verify(httpServletRequest, Mockito.times(1)).getRequestURI();
	}

	@SneakyThrows
	@Test
	void givenNoPercentageAvailable_whenGetPrice_thenThrowsPreconditionFailed() {
		// Arrange
		Mockito.when(percentageService.getCurrent()).thenReturn(null);

		// Act & Assert
		assertThatThrownBy(() -> pricingServiceService.getPrice(1, 2))
				.isInstanceOf(ResponseStatusException.class)
				.hasMessage("412 PRECONDITION_FAILED \"No se pudo encontrar el porcenjate\"");

		Mockito.verify(httpServletRequest, Mockito.times(1)).getRequestURI();
		Mockito.verify(percentageService, Mockito.times(1)).getCurrent();
		Mockito.verify(percentageProcessorService, Mockito.times(1)).saveHistory(Mockito.any());
	}

	@SneakyThrows
	@Test
	void givenJsonProcessingException_whenGetPrice_thenThrowsPreconditionFailed() {
		// Arrange
		Mockito.when(percentageService.getCurrent()).thenReturn(null);
		Mockito.when(objectMapper.writeValueAsString(Mockito.any(Object.class)))
				.thenThrow(new JsonProcessingException("Error") {});

		// Act & Assert
		assertThatThrownBy(() -> pricingServiceService.getPrice(1, 2))
				.isInstanceOf(ResponseStatusException.class)
				.hasMessage("412 PRECONDITION_FAILED \"No se pudo encontrar el porcenjate\"");

		Mockito.verify(httpServletRequest, Mockito.times(1)).getRequestURI();
		Mockito.verify(percentageService, Mockito.times(1)).getCurrent();
		Mockito.verify(percentageProcessorService, Mockito.times(1)).saveHistory(Mockito.any());
	}
}
