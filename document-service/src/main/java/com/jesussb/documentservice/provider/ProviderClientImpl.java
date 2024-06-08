package com.jesussb.documentservice.provider;

import com.jesussb.documentservice.provider.dto.ProviderDTO;
import jakarta.ws.rs.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ProviderClientImpl implements ProviderClient {

    private final WebClient.Builder webClientBuilder;
    private static final String URI = "lb://provider-service/api/v1/providers";

    @Override
    public ProviderDTO findByIdentificationCard(String identificationCard) {
        return this.webClientBuilder.build()
                .get()
                .uri(URI + "/" + identificationCard)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, r -> Mono.error(BadRequestException::new))
                .bodyToMono(ProviderDTO.class)
                .block();
    }

}
