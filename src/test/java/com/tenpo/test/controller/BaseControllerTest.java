package com.tenpo.test.controller;

import com.tenpo.test.config.PorcentageApiMock;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Base class for controller tests.
 */
class BaseControllerTest {

	private PorcentageApiMock porcentageApiMock;
	private static GenericContainer<?> redisServer;

	void init(int port) {
		porcentageApiMock = new PorcentageApiMock(port);
		porcentageApiMock.startMockServer();
		redisServer =
				new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379);
		redisServer.start();
		System.setProperty("spring.data.redis.host", redisServer.getHost());
		System.setProperty("spring.data.redis.port", redisServer.getMappedPort(6379).toString());
	}

	void shutDown() {
		porcentageApiMock.stop();
		redisServer.stop();
	}
}


