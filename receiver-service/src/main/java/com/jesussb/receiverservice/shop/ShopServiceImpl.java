package com.jesussb.receiverservice.shop;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ShopServiceImpl implements ShopService  {

    private final ShopRepository shopRepository;

    @Override
    public Shop findByReceiverIdAndBusinessName(Long receiverId, String businessName) {
        return shopRepository.findByReceiverIdAndBusinessName(receiverId, businessName)
                .orElseThrow(() -> new EntityNotFoundException("Local no existente"));
    }
}
