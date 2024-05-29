package com.jesussb.providerservice.provider;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ProviderServiceImplTest {

    @InjectMocks
    private ProviderServiceImpl providerServiceImpl;
    @Mock
    private ProviderRepository providerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    String identificationCard = "111111111";

    @Test
    void findByIdentificationCard() {

        Provider provider = Provider.builder().identificationCard(identificationCard).build();

        when(providerRepository.findByIdentificationCard(identificationCard)).thenReturn(Optional.of(provider));
        Provider providerTest = providerServiceImpl.findByIdentificationCard(identificationCard);

        assertThat(provider).isEqualTo(providerTest);

    }

    @Test
    void findByIdentificationCardTest() {

        when(providerRepository.findByIdentificationCard(identificationCard)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> providerServiceImpl.findByIdentificationCard(identificationCard));

    }

}