package com.jesussb.documentservice.mailsupplier;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.jesussb.documentservice.mailsupplier.dto.MailSupplierDTO;
import jakarta.ws.rs.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MailSupplierClientImplTest {

    @RegisterExtension
    static WireMockExtension extension = WireMockExtension.newInstance()
            .options(options().dynamicPort())
            .build();

    private WebClient webClient;
    private final String TEST_URL_FIND_ALL_ACTIVE = "/active";
    private final String TEST_URL_GET_ATTACHMENTS = "/attachments/1";


    @BeforeEach
    public void setup() {
        String baseUrl = "http://localhost:" + extension.getPort();
        webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Test
    void findAllActive() {

        String body = """
                [
                {
                      "id": 1,
                      "mailAddress": "example@mail.com",
                      "password": "password123",
                      "host": "smtp.mail.com",
                      "port": "587",
                      "active": true,
                      "createdAt": "2023-06-07T10:15:30Z",
                      "updatedAt": "2023-06-07T12:00:00Z",
                      "receiverId": 2
                },
                {
                  "id": 123,
                  "mailAddress": "example@mail.com",
                  "password": "password123",
                  "host": "smtp.example.com",
                  "port": "587",
                  "active": true,
                  "createdAt": "2023-06-07T12:34:56.789Z",
                  "updatedAt": "2023-06-07T12:34:56.789Z",
                  "receiverId": 456
                }
                ]
                """;

        extension.stubFor(get(urlEqualTo(TEST_URL_FIND_ALL_ACTIVE))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.OK.value())
                        .withBody(body)
                )
        );

        List<MailSupplierDTO> response = webClient.get()
                .uri(TEST_URL_FIND_ALL_ACTIVE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToFlux(MailSupplierDTO.class)
                .collectList()
                .block();

        assertThat(response).isNotNull();
        assertThat(response).hasSize(2);

        assertThat(response.getFirst()).extracting(
                MailSupplierDTO::id,
                MailSupplierDTO::mailAddress,
                MailSupplierDTO::password,
                MailSupplierDTO::host,
                MailSupplierDTO::port,
                MailSupplierDTO::active,
                MailSupplierDTO::createdAt,
                MailSupplierDTO::updatedAt,
                MailSupplierDTO::receiverId
        ).containsExactly(
                1L,
                "example@mail.com",
                "password123",
                "smtp.mail.com",
                "587",
                true,
                Timestamp.from(Instant.parse("2023-06-07T10:15:30Z")),
                Timestamp.from(Instant.parse("2023-06-07T12:00:00Z")),
                2L
        );

        assertThat(response.getLast()).extracting(
                MailSupplierDTO::id,
                MailSupplierDTO::mailAddress,
                MailSupplierDTO::password,
                MailSupplierDTO::host,
                MailSupplierDTO::port,
                MailSupplierDTO::active,
                MailSupplierDTO::createdAt,
                MailSupplierDTO::updatedAt,
                MailSupplierDTO::receiverId
        ).containsExactly(
                123L,
                "example@mail.com",
                "password123",
                "smtp.example.com",
                "587",
                true,
                Timestamp.from(Instant.parse("2023-06-07T12:34:56.789Z")),
                Timestamp.from(Instant.parse("2023-06-07T12:34:56.789Z")),
                456L
        );

        extension.verify(1,
                getRequestedFor(urlEqualTo(TEST_URL_FIND_ALL_ACTIVE))
                        .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
        );

    }

    @Test
    void findAllActiveException() {

        extension.stubFor(get(urlEqualTo(TEST_URL_FIND_ALL_ACTIVE))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                )
        );

        assertThrows(BadRequestException.class, () -> webClient.get()
                .uri(TEST_URL_FIND_ALL_ACTIVE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, r -> Mono.error(BadRequestException::new))
                .bodyToFlux(MailSupplierDTO.class)
                .collectList()
                .block());

        extension.verify(1,
                getRequestedFor(urlEqualTo(TEST_URL_FIND_ALL_ACTIVE))
                        .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
        );

    }

    @Test
    void getAttachments() {

        String body = """
                [
                  "<DTE id=\\"1\\"><Info><Type>Factura</Type><Date>2024-06-07</Date><Amount>100.00</Amount></Info></DTE>",
                  "<DTE id=\\"2\\"><Info><Type>Boleta</Type><Date>2024-06-07</Date><Amount>50.00</Amount></Info></DTE>"
                ]
                """;

        extension.stubFor(get(urlEqualTo(TEST_URL_GET_ATTACHMENTS))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.OK.value())
                        .withBody(body)
                )
        );

        List<String> response = webClient.get()
                .uri(TEST_URL_GET_ATTACHMENTS)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {
                })
                .block();

        assertThat(response).isNotNull();
        assertThat(response).hasSize(2);

        extension.verify(1,
                getRequestedFor(urlEqualTo(TEST_URL_GET_ATTACHMENTS))
                        .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
        );

    }

    @Test
    void getAttachmentsException() {

        extension.stubFor(get(urlEqualTo(TEST_URL_GET_ATTACHMENTS))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                )
        );

        assertThrows(BadRequestException.class, () -> webClient.get()
                .uri(TEST_URL_GET_ATTACHMENTS)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, r -> Mono.error(BadRequestException::new))
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {
                })
                .block());

        extension.verify(1,
                getRequestedFor(urlEqualTo(TEST_URL_GET_ATTACHMENTS))
                        .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
        );


    }

}