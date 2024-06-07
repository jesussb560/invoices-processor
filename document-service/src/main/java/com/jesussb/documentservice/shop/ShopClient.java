package com.jesussb.documentservice.shop;

import com.jesussb.documentservice.shop.dto.ShopDTO;

public interface ShopClient {
    ShopDTO findByReceiverIdAndBusinessName(Long receiverId, String businessName);
}
