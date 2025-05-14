package com.tenpo.test.unit.service.impl;

import com.tenpo.test.dto.history.CalledHistoryDto;
import com.tenpo.test.service.CalledHistoryService;
import com.tenpo.test.service.impl.AsyncPercentageProcessorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.tenpo.test.model.enums.Status.SUCCESSFUL;

@ExtendWith(MockitoExtension.class)
class AsyncPercentageProcessorServiceImplUnitTest {

	@InjectMocks
	private AsyncPercentageProcessorServiceImpl asyncPercentageProcessorService;
	@Mock
	private  CalledHistoryService calledHistoryService;

@Test
	void test_GetPrice_Should_ReturnPrice_When_Invoked() {
		long numOne = 1;
		long numTwo = 2;
		String parameters = String.format("numOne=%d&numTwo=%d", numOne, numTwo);
		var percentageDto = CalledHistoryDto.builder()
				.parameters(parameters)
				.response("response")
				.status(SUCCESSFUL)
				.build();

		asyncPercentageProcessorService.saveHistory(percentageDto);

		Mockito.verify(calledHistoryService, Mockito.times(1)).create(Mockito.any());
	}

}
