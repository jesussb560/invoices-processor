package com.jesussb.receiverservice.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/shops")
public class ShopRestController {

    private final ShopService shopService;

    @GetMapping(value = "/{receiverId}/{businessName}")
    public ResponseEntity<Shop> findByReceiverIdAndBusinessName(@PathVariable Long receiverId, @PathVariable String businessName) {
        return new ResponseEntity<>(shopService.findByReceiverIdAndBusinessName(receiverId, businessName), HttpStatus.OK);
    }

}
