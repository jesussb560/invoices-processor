package com.jesussb.documentservice.document.batch.readers;

import com.jesussb.documentservice.mailsupplier.MailSupplierClient;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@RequiredArgsConstructor
public class RestDocumentReader implements ItemReader<String> {

    private final MailSupplierClient mailSupplierClient;

    @Value("#{jobParameters['mailSupplierId']}")
    private Long mailSupplierId;

    private int next;
    private List<String> attachments;

    @Override
    public String read() throws Exception {

        if (attachments == null || next >= attachments.size()) {
            attachments = mailSupplierClient.getAttachments(mailSupplierId);
            next = 0;
        }

        return attachments != null ? attachments.get(next++) : null;

    }

}
