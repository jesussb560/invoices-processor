package com.jesussb.providerservice.provider;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;

    @Override
    public Provider findByIdentificationCard(String identificationCard) {
        return providerRepository.findByIdentificationCard(identificationCard)
                .orElseThrow(() -> new EntityNotFoundException("Emisor no encontrado."));
    }
}
