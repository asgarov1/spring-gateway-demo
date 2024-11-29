package com.asgarov.gateway_demo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockserver.model.HttpRequest.request;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GatewayDemoApplicationTests {

	static ClientAndServer mockServer;
    @Autowired
    private WebTestClient webTestClient;

	@BeforeAll
	static void startServer() {
		mockServer = ClientAndServer.startClientAndServer(8081);
	}

	@AfterAll
	static void stopServer() {
		mockServer.stop();
	}

	@Test
	void testRoutingToBookingServiceHappyPath() {
		// GIVEN I have mocked an endpoint
		mockServer.when(request().withPath("/bookings/1234"))
				.respond(HttpResponse.response("dummy response"));

		// WHEN I call booking endpoint THEN I get the ok response
		webTestClient.get()
				.uri("/bookings/1234")
				.exchange()
				.expectStatus()
				.isOk();
	}

}
