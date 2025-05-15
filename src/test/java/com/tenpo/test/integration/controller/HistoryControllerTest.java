package com.tenpo.test.integration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.tenpo.test.TestBackendTenpoApplication;
import com.tenpo.test.base.BaseIntegrationControllerTest;
import com.tenpo.test.model.CalledHistory;
import com.tenpo.test.model.enums.Status;
import com.tenpo.test.reposiroty.CalledHistoryRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDateTime;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestBackendTenpoApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class HistoryControllerTest extends BaseIntegrationControllerTest {
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private CalledHistoryRepository calledHistoryRepository;
	@Autowired
	private MockMvc mockMvc;
	private final Faker faker = new Faker();

	@BeforeAll
	void beforeAll() {
		super.init(9091);
	}

	@AfterAll
	void afterAll() {
		super.shutDown();
	}

	@BeforeEach
	void cleanBefore() {
		calledHistoryRepository.deleteAll();
	}

	@AfterEach
	void cleanAfter() {
		calledHistoryRepository.deleteAll();
	}

	private void saveHistory(Object response, Status status) {
		try {
			calledHistoryRepository.save(CalledHistory.builder()
					.response(objectMapper.writeValueAsString(response))
					.status(status)
					.endpoint(faker.company().url())
					.date(LocalDateTime.now())
					.build());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@SneakyThrows
	@Test
	void givenHistoryRecords_whenGetAll_thenReturnsAllPagedResults() {
		// Arrange
		saveHistory("ERROR", Status.FAILED);
		saveHistory("OK", Status.SUCCESSFUL);

		// Act
		ResultActions result = mockMvc.perform(
				MockMvcRequestBuilders.get("/v1/history/index")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
		).andDo(MockMvcResultHandlers.print());

		// Assert
		result.andExpectAll(
				MockMvcResultMatchers.status().isOk(),
				MockMvcResultMatchers.jsonPath("$.totalElements").value(2),
				MockMvcResultMatchers.jsonPath("$.totalPages").value(1)
		);
	}

	@SneakyThrows
	@Test
	void givenHistoryRecords_whenGetSuccessful_thenReturnsOnlySuccessful() {
		// Arrange
		saveHistory("ERROR", Status.FAILED);
		saveHistory("OK", Status.SUCCESSFUL);

		// Act
		ResultActions result = mockMvc.perform(
				MockMvcRequestBuilders.get("/v1/history/index?status=SUCCESSFUL")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
		).andDo(MockMvcResultHandlers.print());

		// Assert
		result.andExpectAll(
				MockMvcResultMatchers.status().isOk(),
				MockMvcResultMatchers.jsonPath("$.totalElements").value(1),
				MockMvcResultMatchers.jsonPath("$.totalPages").value(1)
		);
	}

	@SneakyThrows
	@Test
	void givenHistoryRecords_whenGetFailed_thenReturnsOnlyFailed() {
		// Arrange
		saveHistory("ERROR", Status.FAILED);
		saveHistory("OK", Status.SUCCESSFUL);

		// Act
		ResultActions result = mockMvc.perform(
				MockMvcRequestBuilders.get("/v1/history/index?status=FAILED")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
		).andDo(MockMvcResultHandlers.print());

		// Assert
		result.andExpectAll(
				MockMvcResultMatchers.status().isOk(),
				MockMvcResultMatchers.jsonPath("$.totalElements").value(1),
				MockMvcResultMatchers.jsonPath("$.totalPages").value(1)
		);
	}
}


