package com.jesussb.providerservice.provider;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v1/providers")
public class ProviderRestController {

    private final ProviderService providerService;

    @GetMapping(value = "/{identificationCard}")
    public ResponseEntity<Provider> findByIdentificationCard(@PathVariable String identificationCard){
        return new ResponseEntity<>(providerService.findByIdentificationCard(identificationCard), HttpStatus.OK);
    }

}
