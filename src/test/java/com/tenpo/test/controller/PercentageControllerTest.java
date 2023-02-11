package com.tenpo.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.test.TestBackendTenpoApplication;
import com.tenpo.test.config.PorcentageApiMock;
import com.tenpo.test.dto.pricing.PricingDto;
import com.tenpo.test.reposiroty.CalledHistoryRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
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
import redis.embedded.RedisServerBuilder;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestBackendTenpoApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PercentageControllerTest {
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private CalledHistoryRepository calledHistoryRepository;
	@Autowired
	private MockMvc mockMvc;
	private PorcentageApiMock porcentageApiMock = new PorcentageApiMock(9090);

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

	@Test
	@Order(1)
	void test_Caculate_Should_ReturnBadRequestNegativeValue_When_Invoked() throws JsonProcessingException, Exception {
		mockMvc.perform(
						MockMvcRequestBuilders.get("/v1/pricing/caculate/-1/2")
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpectAll(
						MockMvcResultMatchers.status().isBadRequest()

				);

		calledHistoryRepository.deleteAll();
	}

	@Test
	@Order(2)
	void test_Caculate_Should_ReturnBadRequest_When_Invoked() throws JsonProcessingException, Exception {
		mockMvc.perform(
						MockMvcRequestBuilders.get("/v1/pricing/caculate/fsdfsdfdsfds/2")
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpectAll(
						MockMvcResultMatchers.status().isBadRequest()

				);

		calledHistoryRepository.deleteAll();
	}



	@Test
	@Order(3)
	void test_Caculate_Should_ShowPrice_When_Invoked() throws JsonProcessingException, Exception {
		ResultActions res = mockMvc.perform(
						MockMvcRequestBuilders.get("/v1/pricing/caculate/1/2")
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpectAll(
						MockMvcResultMatchers.status().isOk()

				);
		Assertions.assertNotNull(res);
		Assertions.assertNotNull(res.andReturn());
		Assertions.assertNotNull(res.andReturn().getResponse());
		Assertions.assertNotNull(res.andReturn().getResponse().getContentAsString());
		PricingDto pricingDto = objectMapper.readValue(res.andReturn().getResponse().getContentAsString(), PricingDto.class);
		Assertions.assertNotNull(pricingDto);
		Assertions.assertNotNull(pricingDto.getResult());
		Assertions.assertEquals("6",pricingDto.getResult());

		calledHistoryRepository.deleteAll();
	}


	@Test
	@Order(4)
	void test_Caculate_Should_ReturnTooManyRequests_When_Invoked() throws JsonProcessingException, Exception {
		mockMvc.perform(
						MockMvcRequestBuilders.get("/v1/pricing/caculate/1/2")
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpectAll(
						MockMvcResultMatchers.status().isOk()

				);
		mockMvc.perform(
						MockMvcRequestBuilders.get("/v1/pricing/caculate/1/2")
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpectAll(
						MockMvcResultMatchers.status().isOk()

				);
		mockMvc.perform(
						MockMvcRequestBuilders.get("/v1/pricing/caculate/1/2")
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpectAll(
						MockMvcResultMatchers.status().isTooManyRequests()

				);

		calledHistoryRepository.deleteAll();
	}

}


