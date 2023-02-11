package com.tempo.test.controller;

import com.tempo.test.dto.history.CalledHistoryDto;
import com.tempo.test.model.CalledHistory;
import com.tempo.test.model.enums.Status;
import com.tempo.test.service.CalledHistoryService;
import com.tempo.test.service.RateLimitService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.swagger.v3.oas.annotations.tags.Tag;
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
public class HistoryController {

	private final Bucket bucket;
	private final CalledHistoryService calledHistoryService;
	private final RateLimitService rateLimitService;

	public  HistoryController(Bandwidth limit, CalledHistoryService calledHistoryService, RateLimitService rateLimitService){
		this.rateLimitService = rateLimitService;
		this.calledHistoryService = calledHistoryService;
		bucket =  Bucket.builder()
				.addLimit(limit)
				.build();
	}

	@GetMapping(value = "/index")
	public ResponseEntity<Page<CalledHistoryDto>>  index(
			@RequestParam(required = false) Status status,
			Pageable page
	) {
		rateLimitService.validate(bucket);
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("status", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		Example<CalledHistory> example = Example.of(
				CalledHistory.builder().status(status).build(),
				exampleMatcher);
		return ResponseEntity.ok().body(calledHistoryService.index(example, page));
	}
}
