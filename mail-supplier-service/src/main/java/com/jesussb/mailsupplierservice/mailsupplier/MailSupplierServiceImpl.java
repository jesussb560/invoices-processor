package com.jesussb.mailsupplierservice.mailsupplier;

import com.jesussb.mailsupplierservice.commons.mail.MailReader;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MailSupplierServiceImpl implements MailSupplierService{

    private final MailSupplierRepository mailSupplierRepository;
    private final MailReader mailReader;

    @Override
    public List<MailSupplier> findAllActive() {
        return mailSupplierRepository.findAllActive();
    }

    @Override
    public List<String> getAttachments(Long id) {

        MailSupplier mailSupplier = mailSupplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(""));

        return mailReader.getMailSupplierAttachments(mailSupplier);
    }

}
