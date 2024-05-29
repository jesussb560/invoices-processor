package com.jesussb.receiverservice.shop;

import java.util.Optional;

public interface ShopService {

    Shop findByReceiverIdAndBusinessName(Long receiverId, String businessName);

}
