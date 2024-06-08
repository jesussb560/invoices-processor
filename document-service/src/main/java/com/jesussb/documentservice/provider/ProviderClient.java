package com.jesussb.documentservice.provider;

import com.jesussb.documentservice.provider.dto.ProviderDTO;

public interface ProviderClient {

    ProviderDTO findByIdentificationCard(String identificationCard);

}
