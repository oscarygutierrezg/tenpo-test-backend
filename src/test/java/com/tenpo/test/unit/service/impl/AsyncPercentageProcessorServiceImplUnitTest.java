package com.tenpo.test.unit.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.test.dto.percentage.PercentageDto;
import com.tenpo.test.service.CalledHistoryService;
import com.tenpo.test.service.impl.AsyncPercentageProcessorServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.tenpo.test.model.enums.Status.SUCCESSFUL;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AsyncPercentageProcessorServiceImplUnitTest {

	@InjectMocks
	private AsyncPercentageProcessorServiceImpl asyncPercentageProcessorService;
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

		asyncPercentageProcessorService.saveHistory(percentageDto,SUCCESSFUL);

		Mockito.verify(calledHistoryService, Mockito.times(1)).create(Mockito.any());
		Mockito.verify(httpServletRequest, Mockito.times(1)).getRequestURI();
	}

	@SneakyThrows
	@Test
	void test_GetPrice_Should_ReturnPriceAndErrorSaveHistory_When_Invoked() {
		var percentageDto = new PercentageDto();
		percentageDto.setValue(1D);
		Mockito.when(objectMapper.writeValueAsString(Mockito.any(Object.class))).thenThrow(new JsonProcessingException("Error"){});

		asyncPercentageProcessorService.saveHistory(percentageDto,SUCCESSFUL);

		Mockito.verify(objectMapper, Mockito.times(1)).writeValueAsString(Mockito.any());
		Mockito.verify(calledHistoryService, Mockito.times(0)).create(Mockito.any());
		Mockito.verify(httpServletRequest, Mockito.times(0)).getRequestURI();
	}

}
