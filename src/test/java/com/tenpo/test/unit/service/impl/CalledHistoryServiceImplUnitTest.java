package com.tenpo.test.unit.service.impl;

import com.tenpo.test.dto.history.CalledHistoryDto;
import com.tenpo.test.dto.history.CalledHistoryMapper;
import com.tenpo.test.model.CalledHistory;
import com.tenpo.test.reposiroty.CalledHistoryRepository;
import com.tenpo.test.service.impl.CalledHistoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class CalledHistoryServiceImplUnitTest {

	@InjectMocks
	private CalledHistoryServiceImpl calledHistoryService;
	@Mock
	private CalledHistoryRepository calledHistoryRepository;
	@Spy
	private CalledHistoryMapper calledHistoryMapper;

	@Test
	void givenValid_whenCreate_thenSavesCalledHistory() {
		// Arrange
		var dto = CalledHistoryDto.builder().build();
		Mockito.when(calledHistoryMapper.toModel(Mockito.any(CalledHistoryDto.class))).thenAnswer(p -> toModel((CalledHistoryDto) p.getArguments()[0]));
		Mockito.when(calledHistoryRepository.save(Mockito.any(CalledHistory.class))).thenReturn(new CalledHistory());

		// Act
		calledHistoryService.create(dto);

		// Assert
		Mockito.verify(calledHistoryMapper, Mockito.times(1)).toModel(Mockito.any());
		Mockito.verify(calledHistoryRepository, Mockito.times(1)).save(Mockito.any());
	}

	@Test
	void givenRepositoryWithData_whenIndex_thenReturnsPageOfDtos() {
		// Arrange
		Page<CalledHistory> page = new PageImpl<>(List.of(new CalledHistory(), new CalledHistory(), new CalledHistory()));
		Mockito.when(calledHistoryRepository.findAll(Mockito.any(Example.class), Mockito.any(Pageable.class))).thenReturn(page);
		Mockito.when(calledHistoryMapper.toDto(Mockito.any(CalledHistory.class))).thenAnswer(p -> toDto((CalledHistory) p.getArguments()[0]));

		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("status", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		Example<CalledHistory> example = Example.of(
				CalledHistory.builder().status(null).build(),
				exampleMatcher);
		Pageable pageable = PageRequest.of(0, 3);

		// Act
		var result = calledHistoryService.index(example, pageable);

		// Assert
		Assertions.assertNotNull(result);
		Assertions.assertNotNull(result.getContent());
		Assertions.assertEquals(3, result.getContent().size());
		Mockito.verify(calledHistoryMapper, Mockito.times(3)).toDto(Mockito.any());
		Mockito.verify(calledHistoryRepository, Mockito.times(1)).findAll(Mockito.any(Example.class), Mockito.any(Pageable.class));
	}

	// Métodos utilitarios privados
	private CalledHistoryDto toDto(CalledHistory calledHistory) {
		if (calledHistory == null) {
			return null;
		}
		return CalledHistoryDto.builder()
				.endpoint(calledHistory.getEndpoint())
				.response(calledHistory.getResponse())
				.status(calledHistory.getStatus())
				.date(calledHistory.getDate())
				.parameters(calledHistory.getParameters())
				.build();
	}

	private CalledHistory toModel(CalledHistoryDto calledHistory) {
		if (calledHistory == null) {
			return null;
		}
		return CalledHistory.builder()
				.endpoint(calledHistory.getEndpoint())
				.response(calledHistory.getResponse())
				.status(calledHistory.getStatus())
				.date(calledHistory.getDate())
				.parameters(calledHistory.getParameters())
				.build();
	}
}