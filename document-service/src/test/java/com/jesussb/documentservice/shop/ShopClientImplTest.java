package com.jesussb.documentservice.shop;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.jesussb.documentservice.shop.dto.ShopDTO;
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

class ShopClientImplTest {

    @RegisterExtension
    static WireMockExtension extension = WireMockExtension.newInstance().options(options().port(8089)).build();

    private WebClient webClient;

    String testUrl = "/1/businessTestName";

    @BeforeEach
    void setUp() {
        webClient = WebClient.builder().baseUrl("http://localhost:8089").build();
    }

    @Test
    void findByReceiverIdAndBusinessName() {

        String body = """
                        {
                            "id":1
                        }
                """;

        extension.stubFor(get(urlEqualTo(testUrl))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.OK.value())
                        .withBody(body)
                ));

        ShopDTO response = webClient.get()
                .uri(testUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(ShopDTO.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);

        extension.verify(1,
                getRequestedFor(urlEqualTo(testUrl))
                        .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
        );

    }

    @Test
    void findByReceiverIdAndBusinessNameException() {

        extension.stubFor(get(urlEqualTo(testUrl))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                ));

        assertThrows(BadRequestException.class, () -> webClient.get()
                .uri(testUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, r -> Mono.error(BadRequestException::new))
                .bodyToMono(ShopDTO.class)
                .block());

        extension.verify(1, getRequestedFor(urlEqualTo(testUrl))
                .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
        );

    }

}