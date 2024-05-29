package com.jesussb.receiverservice.shop;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    Optional<Shop> findByReceiverIdAndBusinessName(Long receiverId, String businessName);

}
