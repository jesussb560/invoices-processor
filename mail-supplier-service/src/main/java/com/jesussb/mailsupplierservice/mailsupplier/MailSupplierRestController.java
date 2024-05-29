package com.jesussb.mailsupplierservice.mailsupplier;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/mail-suppliers")
public class MailSupplierRestController {

    private final MailSupplierService mailSupplierService;

    @GetMapping(value = "/active")
    public ResponseEntity<List<MailSupplier>> findAllActive(){
        return new ResponseEntity<>(mailSupplierService.findAllActive(), HttpStatus.OK);
    }

    @GetMapping(value = "/get-attachments/{id}")
    public ResponseEntity<List<String>> getAttachments(@PathVariable Long id){
        return new ResponseEntity<>(mailSupplierService.getAttachments(id), HttpStatus.OK);
    }

}
