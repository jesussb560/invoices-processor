package com.jesussb.receiverservice.shop;

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


class ShopServiceImplTest {

    @InjectMocks
    private ShopServiceImpl shopServiceImpl;
    @Mock
    private ShopRepository shopRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    String businessName = "test";
    Long id = 1L;

    @Test
    void findByReceiverIdAndBusinessName() {

        Shop shop = Shop.builder().businessName("test business").build();

        when(shopRepository.findByReceiverIdAndBusinessName(id, businessName)).thenReturn(Optional.of(shop));
        Shop shopTest = shopServiceImpl.findByReceiverIdAndBusinessName(id, businessName);

        assertThat(shop).isEqualTo(shopTest);

    }

    @Test
    void findByReceiverIdAndBusinessNameException() {

        when(shopRepository.findByReceiverIdAndBusinessName(id, businessName)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> shopServiceImpl.findByReceiverIdAndBusinessName(id, businessName));

    }

}