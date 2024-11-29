package com.asgarov.gateway_demo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URI;

import static org.mockserver.model.HttpRequest.request;

@Import(GatewayDemoApplicationTestsTwo.TestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GatewayDemoApplicationTestsTwo {

	static ClientAndServer mockServer;

	@AfterAll
	static void stopServer() {
		mockServer.stop();
	}

    @Autowired
    private WebTestClient webTestClient;

	@Autowired
	GatewayProperties gatewayProperties;

	@Autowired
	ApplicationEventPublisher applicationEventPublisher;

	@Test
	void testRoutingToBookingServiceHappyPath() {
		// GIVEN I have mocked an endpoint
		mockServer.when(request().withPath("/bookings/1234"))
				.respond(HttpResponse.response("dummy response"));

		// AND I updated my routes
		gatewayProperties.getRoutes()
						.stream()
				.filter(route -> route.getId().equals("booking-route"))
				.forEach(route -> route.setUri(URI.create("http://localhost:" + mockServer.getPort())));
		applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));

		// WHEN I call booking endpoint THEN I get the ok response
		webTestClient.get()
				.uri("/bookings/1234")
				.exchange()
				.expectStatus()
				.isOk();
	}

	@Component
	static class TestConfig {
		@EventListener(WebServerInitializedEvent.class)
		void initializeMockServer(WebServerInitializedEvent event) {
			int port = event.getWebServer().getPort();
			mockServer = ClientAndServer.startClientAndServer(port + 1, port + 2, port + 3);
		}
	}

}
