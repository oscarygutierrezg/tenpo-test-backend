package com.tenpo.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.tenpo.test.TestBackendTenpoApplication;
import com.tenpo.test.config.PorcentageApiMock;
import com.tenpo.test.model.CalledHistory;
import com.tenpo.test.model.enums.Status;
import com.tenpo.test.reposiroty.CalledHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import redis.embedded.RedisServerBuilder;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestBackendTenpoApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class HistoryControllerTest {
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private CalledHistoryRepository calledHistoryRepository;
	@Autowired
	private MockMvc mockMvc;
	private PorcentageApiMock porcentageApiMock = new PorcentageApiMock(9090);
	public Faker faker = new Faker();
	private static redis.embedded.RedisServer redisServer;



	@BeforeAll
	void init() {
		porcentageApiMock.startMockServer();
		redisServer = new RedisServerBuilder().port(6370).setting("maxmemory 256M").build();
		redisServer.start();
	}

	@AfterAll
	void shutDown() {
		porcentageApiMock.stop();
		redisServer.stop();
	}

	public void saveHistory(Object response, Status status) {
		try{
			calledHistoryRepository.save(CalledHistory.builder()
					.response(objectMapper.writeValueAsString(response))
					.status(status)
					.url(faker.company().url())
					.build());
		} catch (Exception e){
			log.error(e.getMessage(),e);
		}
	}


	@Test
	void test_Caculate_Should_ReturnPage_When_Invoked() throws JsonProcessingException, Exception {
		saveHistory("ERROR", Status.FAILED);
		saveHistory("OK", Status.SUCCESSFUL);
		mockMvc.perform(
						MockMvcRequestBuilders.get("/v1/history/index")
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpectAll(
						MockMvcResultMatchers.status().isOk()

				).andExpectAll(
						MockMvcResultMatchers.status().isOk(),
						MockMvcResultMatchers.jsonPath("$.totalElements").value(2),
						MockMvcResultMatchers.jsonPath("$.totalPages").value(1)
				);

		mockMvc.perform(
						MockMvcRequestBuilders.get("/v1/history/index?status=SUCCESSFUL")
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpectAll(
						MockMvcResultMatchers.status().isOk()

				).andExpectAll(
						MockMvcResultMatchers.status().isOk(),
						MockMvcResultMatchers.jsonPath("$.totalElements").value(1),
						MockMvcResultMatchers.jsonPath("$.totalPages").value(1)
				);

		mockMvc.perform(
						MockMvcRequestBuilders.get("/v1/history/index?status=FAILED")
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpectAll(
						MockMvcResultMatchers.status().isOk()

				).andExpectAll(
						MockMvcResultMatchers.status().isOk(),
						MockMvcResultMatchers.jsonPath("$.totalElements").value(1),
						MockMvcResultMatchers.jsonPath("$.totalPages").value(1)
				);
		calledHistoryRepository.deleteAll();
	}
}


