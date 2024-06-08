package com.jesussb.documentservice.mailsupplier;

import com.jesussb.documentservice.mailsupplier.dto.MailSupplierDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailSupplierClientImpl implements MailSupplierClient {

    private final WebClient.Builder webClientBuilder;
    private static final String URI = "lb://mail-supplier-service/api/v1/mail-suppliers";

    @Override
    public List<MailSupplierDTO> findAllActive() {
        return this.webClientBuilder.build()
                .get()
                .uri(URI + "/active")
                .retrieve()
                .bodyToFlux(MailSupplierDTO.class)
                .collectList()
                .block();
    }

    @Override
    public List<String> getAttachments(Long id) {

        return this.webClientBuilder.build()
                .get()
                .uri(URI + "/get-attachments/" + 1)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {
                })
                .block();
    }

}
