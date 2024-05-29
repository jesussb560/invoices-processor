package com.jesussb.mailsupplierservice.mailsupplier;

import java.util.List;

public interface MailSupplierService {

    List<MailSupplier> findAllActive();

    List<String> getAttachments(Long id);

}
