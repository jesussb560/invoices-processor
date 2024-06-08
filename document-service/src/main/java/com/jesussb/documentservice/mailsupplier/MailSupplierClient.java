package com.jesussb.documentservice.mailsupplier;

import com.jesussb.documentservice.mailsupplier.dto.MailSupplierDTO;

import java.util.List;

public interface MailSupplierClient {

    List<MailSupplierDTO> findAllActive();

    List<String> getAttachments(Long id);

}
