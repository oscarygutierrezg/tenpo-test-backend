package com.tenpo.test.unit.service.impl;

import com.tenpo.test.client.PercentageClient;
import com.tenpo.test.dto.percentage.PercentageDto;
import com.tenpo.test.service.impl.PercentageServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PercentageServiceImplUnitTest {

	@InjectMocks
	private PercentageServiceImpl percentageService;
	@Mock
	private PercentageClient percentageClient;


	@SneakyThrows
	@Test
	void givenPercentageAvailable_whenGetCurrent_thenReturnsPercentage() {
		// Arrange
		var percentage = new PercentageDto();
		Mockito.when(percentageClient.getCurrent()).thenReturn(percentage);

		// Act
		var result = percentageService.getCurrent();

		// Assert
		Assertions.assertEquals(percentage, result);
		Mockito.verify(percentageClient, Mockito.times(1)).getCurrent();
	}

}
