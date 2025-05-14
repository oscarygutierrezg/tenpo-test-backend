package com.tenpo.test.controller;

import com.tenpo.test.dto.history.CalledHistoryDto;
import com.tenpo.test.model.CalledHistory;
import com.tenpo.test.model.enums.Status;
import com.tenpo.test.service.CalledHistoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "history")
@RestController
@RequestMapping(value = "/v1/history")
@AllArgsConstructor
public class HistoryController {

	private final CalledHistoryService calledHistoryService;


	@GetMapping(value = "/index")
	public ResponseEntity<Page<CalledHistoryDto>>  index(
			@RequestParam(required = false) Status status,
			Pageable page
	) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("status", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		Example<CalledHistory> example = Example.of(
				CalledHistory.builder().status(status).build(),
				exampleMatcher);
		return ResponseEntity.ok().body(calledHistoryService.index(example, page));
	}
}
