package com.jesussb.documentservice.document;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api/v1/documents")
@RequiredArgsConstructor
@RestController
public class DocumentRestController {

    private final DocumentService documentService;

    @GetMapping(value = "/process")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(documentService.processMailDocuments(), HttpStatus.OK);
    }

}
