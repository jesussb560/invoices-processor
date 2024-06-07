package com.jesussb.documentservice.shop;

import com.jesussb.documentservice.shop.dto.ShopDTO;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ShopClientImpl implements ShopClient {

    private final WebClient.Builder webClientBuilder;
    private static final String URI = "lb://receiver-service/api/v1/shops";

    @Override
    public ShopDTO findByReceiverIdAndBusinessName(Long receiverId, String businessName) {
        return this.webClientBuilder
                .build()
                .get()
                .uri(URI + "/" + receiverId + "/" + businessName)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, r -> Mono.error(BadRequestException::new))
                .bodyToMono(ShopDTO.class)
                .block();
    }
}
