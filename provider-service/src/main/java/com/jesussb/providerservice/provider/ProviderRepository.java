package com.jesussb.providerservice.provider;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderRepository extends JpaRepository<Provider, Long> {

    Optional<Provider> findByIdentificationCard(String identificationCard);

}
