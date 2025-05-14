package com.tenpo.test.service.impl;

import com.tenpo.test.client.PercentageClient;
import com.tenpo.test.dto.percentage.PercentageDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PercentageServiceImplTest {

	@InjectMocks
	private PercentageServiceImpl percentageService;
	@Mock
	private PercentageClient percentageClient;

	@SneakyThrows
	@Test
	void test_GetCurrent_Should_ReturnPercentage_When_Invoked() {
		var percentage = new PercentageDto();
		Mockito.when(percentageClient.getCurrent()).thenReturn(percentage);

		var result = percentageService.getCurrent();

		Assertions.assertEquals(percentage,result);
		Mockito.verify(percentageClient, Mockito.times(1)).getCurrent();
	}

}
