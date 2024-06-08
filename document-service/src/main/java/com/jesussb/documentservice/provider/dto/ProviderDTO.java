package com.jesussb.documentservice.provider.dto;

import jakarta.persistence.Column;

public record ProviderDTO(
        Long id,
        String identificationCard,
        String phone,
        String businessName,
        String economicAct,
        String emailAddress,
        String address,
        String commune,
        String City
) {
}
