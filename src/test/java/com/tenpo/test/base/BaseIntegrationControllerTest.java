package com.tenpo.test.base;

import com.tenpo.test.config.PorcentageApiMock;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Base class for controller tests.
 */
public class BaseIntegrationControllerTest {

	private PorcentageApiMock porcentageApiMock;
	private static GenericContainer<?> redisServer;

	protected void init(int port) {
		porcentageApiMock = new PorcentageApiMock(port);
		redisServer =
				new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379);
		redisServer.start();
		System.setProperty("spring.data.redis.host", redisServer.getHost());
		System.setProperty("spring.data.redis.port", redisServer.getMappedPort(6379).toString());
	}

	protected void shutDown() {
		porcentageApiMock.stop();
		redisServer.stop();
	}

	protected void stopMocks() {
		porcentageApiMock.stop();
	}

	protected void startMocks() {
		porcentageApiMock.startMockServer();
	}

	protected void stopDbs() {
		redisServer.stop();
	}

	protected void startDbs() {
		redisServer.start();
	}
}


