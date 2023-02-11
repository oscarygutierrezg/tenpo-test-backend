package com.tempo.test.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tempo.test.client.PercentageClient;
import com.tempo.test.dto.percentage.PercentageDto;
import com.tempo.test.reposiroty.StringRedisRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class PercentageServiceImplTest {

	@InjectMocks
	private PercentageServiceImpl percentageService;
	@Mock
	private PercentageClient percentageClient;
	@Mock
	private StringRedisRepository stringRedisRepository;
	@Mock
	private ObjectMapper objectMapper;


	@SneakyThrows
	@Test
	void test_SaveCurrent_Should_SavedPercentage_When_Invoked() {
		Mockito.when(objectMapper.writeValueAsString(Mockito.any(Object.class))).thenReturn("");
		Mockito.when(percentageClient.getCurrent()).thenReturn(new PercentageDto());

		percentageService.saveCurrent();

		Mockito.verify(objectMapper, Mockito.times(1)).writeValueAsString(Mockito.any());
		Mockito.verify(percentageClient, Mockito.times(1)).getCurrent();
		Mockito.verify(stringRedisRepository, Mockito.times(1)).add(Mockito.anyString(),Mockito.anyString());
	}

	@SneakyThrows
	@Test
	void test_SaveCurrent_Should_NotSavedPercentage_When_Invoked() {
		Mockito.when(percentageClient.getCurrent()).thenReturn(null);

		percentageService.saveCurrent();

		Mockito.verify(objectMapper, Mockito.times(0)).writeValueAsString(Mockito.any());
		Mockito.verify(stringRedisRepository, Mockito.times(0)).add(Mockito.anyString(),Mockito.anyString());
		Mockito.verify(percentageClient, Mockito.times(1)).getCurrent();
	}

	@Test
	void test_SaveCurrent_Should_Throw_JsonProcessingException_When_Invoked() throws JsonProcessingException {
		Mockito.when(objectMapper.writeValueAsString(Mockito.any(Object.class))).thenThrow(new JsonProcessingException("Error"){});
		Mockito.when(percentageClient.getCurrent()).thenReturn(new PercentageDto());

		assertThatThrownBy(() -> percentageService.saveCurrent())
				.isInstanceOf(JsonProcessingException.class)
				.hasMessage("Error");

		Mockito.verify(objectMapper, Mockito.times(1)).writeValueAsString(Mockito.any());
		Mockito.verify(stringRedisRepository, Mockito.times(0)).add(Mockito.anyString(),Mockito.anyString());
		Mockito.verify(percentageClient, Mockito.times(1)).getCurrent();
	}



}
