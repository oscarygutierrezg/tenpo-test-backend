package com.tenpo.test.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.test.TestBackendTenpoApplication;
import com.tenpo.test.base.BaseIntegrationControllerTest;
import com.tenpo.test.dto.pricing.PricingDto;
import com.tenpo.test.reposiroty.CalledHistoryRepository;
import lombok.SneakyThrows;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestBackendTenpoApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PercentageControllerTest extends BaseIntegrationControllerTest {

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private CalledHistoryRepository calledHistoryRepository;
	@Autowired
	private MockMvc mockMvc;

	@BeforeAll
	void beforeAll() {
		super.init(9091);
	}

	@AfterAll
	void afterAll() {
		super.shutDown();
	}

	@SneakyThrows
	@Test
	@Order(1)
	void givenNoPercentage_whenCalculatePrice_thenReturnsPreconditionFailed() {
		// Arrange

		// Act
		ResultActions result = mockMvc.perform(
				MockMvcRequestBuilders.get("/v1/pricing/caculate/1/2")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
		).andDo(MockMvcResultHandlers.print());

		// Assert
		result.andExpectAll(
				MockMvcResultMatchers.status().isPreconditionFailed()
		);
		calledHistoryRepository.deleteAll();
	}

	@SneakyThrows
	@Test
	@Order(2)
	void givenNegativeValue_whenCalculatePrice_thenReturnsBadRequest() {
		// Arrange

		// Act
		ResultActions result = mockMvc.perform(
				MockMvcRequestBuilders.get("/v1/pricing/caculate/-1/2")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
		).andDo(MockMvcResultHandlers.print());

		// Assert
		result.andExpectAll(
				MockMvcResultMatchers.status().isBadRequest()
		);
		calledHistoryRepository.deleteAll();
	}

	@SneakyThrows
	@Test
	@Order(3)
	void givenNonNumericValue_whenCalculatePrice_thenReturnsBadRequest() {
		// Arrange

		// Act
		ResultActions result = mockMvc.perform(
				MockMvcRequestBuilders.get("/v1/pricing/caculate/fsdfsdfdsfds/2")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
		).andDo(MockMvcResultHandlers.print());

		// Assert
		result.andExpectAll(
				MockMvcResultMatchers.status().isBadRequest()
		);
		calledHistoryRepository.deleteAll();
	}

	@SneakyThrows
	@Test
	@Order(4)
	void givenValidPercentage_whenCalculatePrice_thenReturnsCorrectPrice() {
		// Arrange
		startMocks();

		// Act
		ResultActions result = mockMvc.perform(
				MockMvcRequestBuilders.get("/v1/pricing/caculate/1/2")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
		).andDo(MockMvcResultHandlers.print());

		// Assert
		result.andExpectAll(
				MockMvcResultMatchers.status().isOk()
		);
		Assertions.assertNotNull(result);
		Assertions.assertNotNull(result.andReturn());
		Assertions.assertNotNull(result.andReturn().getResponse());
		Assertions.assertNotNull(result.andReturn().getResponse().getContentAsString());
		PricingDto pricingDto = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), PricingDto.class);
		Assertions.assertNotNull(pricingDto);
		Assertions.assertNotNull(pricingDto.getResult());
		Assertions.assertEquals("6", pricingDto.getResult());

		calledHistoryRepository.deleteAll();
		stopMocks();
	}

}


