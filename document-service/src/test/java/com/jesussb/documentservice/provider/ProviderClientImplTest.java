package com.jesussb.documentservice.provider;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.jesussb.documentservice.provider.dto.ProviderDTO;
import jakarta.ws.rs.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProviderClientImplTest {

    @RegisterExtension
    static WireMockExtension extension = WireMockExtension.newInstance()
            .options(options().dynamicPort())
            .build();

    private WebClient webClient;
    private static final String TEST_URL = "/10293843-1";

    @BeforeEach
    void setUp() {
        String baseUrl = "http://localhost:" + extension.getPort();
        webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Test
    void findByIdentificationCard() {

        String body = """
                {
                "id": 1,
                "identificationCard": "10293843-1",
                "phone": "123456789",
                "businessName": "TEST",
                "economicAct": "TEST",
                "emailAddress": "test@mail.com",
                "address": "fake street 123",
                "commune": "Somewhere",
                "City": "Some random city"
                }
                 """;

        extension.stubFor(get(urlEqualTo(TEST_URL))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.OK.value())
                        .withBody(body)
                )
        );

        ProviderDTO response = webClient.get()
                .uri(TEST_URL)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(ProviderDTO.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.identificationCard()).isEqualTo("10293843-1");
        assertThat(response.phone()).isEqualTo("123456789");
        assertThat(response.businessName()).isEqualTo("TEST");
        assertThat(response.economicAct()).isEqualTo("TEST");
        assertThat(response.emailAddress()).isEqualTo("test@mail.com");
        assertThat(response.address()).isEqualTo("fake street 123");
        assertThat(response.commune()).isEqualTo("Somewhere");
        assertThat(response.City()).isEqualTo("Some random city");

        extension.verify(1,
                getRequestedFor(urlEqualTo(TEST_URL))
                        .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
        );

    }

    @Test
    void findByIdentificationCardException() {

        extension.stubFor(get(urlEqualTo(TEST_URL))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                )
        );

        assertThrows(BadRequestException.class, () -> webClient.get()
                .uri(TEST_URL)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, r -> Mono.error(BadRequestException::new))
                .bodyToMono(ProviderDTO.class)
                .block());

        extension.verify(1,
                getRequestedFor(urlEqualTo(TEST_URL))
                        .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
        );

    }

}