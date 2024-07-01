package com.jesussb.documentservice.document;

import com.jesussb.documentservice.document.dto.MailXmlDocument;
import com.jesussb.documentservice.documentstatus.DocumentStatus;
import com.jesussb.documentservice.documentstatus.DocumentStatusName;
import com.jesussb.documentservice.documentstatus.DocumentStatusRepository;
import com.jesussb.documentservice.documenttype.DocumentType;
import com.jesussb.documentservice.documenttype.DocumentTypeRepository;
import com.jesussb.documentservice.header.Header;
import com.jesussb.documentservice.header.HeaderRepository;
import com.jesussb.documentservice.iddoc.IdDoc;
import com.jesussb.documentservice.iddoc.IdDocRepository;
import com.jesussb.documentservice.mailsupplier.MailSupplierClient;
import com.jesussb.documentservice.mailsupplier.dto.MailSupplierDTO;
import com.jesussb.documentservice.provider.ProviderClient;
import com.jesussb.documentservice.provider.dto.ProviderDTO;
import com.jesussb.documentservice.shop.ShopClient;
import com.jesussb.documentservice.shop.dto.ShopDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentStatusRepository documentStatusRepository;
    private final HeaderRepository headerRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final IdDocRepository idDocRepository;

    private final MailSupplierClient mailSupplierClient;
    private final ShopClient shopClient;
    private final ProviderClient providerClient;

    private final DocumentMapper documentMapper;

    private final JobLauncher jobLauncher;
    private final Job processDocumentJob;

    @Override
    public String processMailDocuments() {

        mailSupplierClient.findAllActive().forEach(this::callProcessDocumentsJob);
        return "documents processed.";

    }

    @Override
    public void saveDocuments(List<MailXmlDocument> xmlDocuments, Long receiverId) {

        List<Document> mappedDocuments = documentMapper.toEntityList(xmlDocuments);

        DocumentStatus documentStatus = documentStatusRepository.findByName(DocumentStatusName.SAVED)
                .orElseThrow(() -> new EntityNotFoundException(""));

        mappedDocuments.forEach(document -> {
            document.setDocumentStatus(documentStatus);
        });

        documentRepository.saveAll(mappedDocuments).forEach(doc -> {

            try {

                MailXmlDocument xmlDocument = xmlDocuments
                        .stream()
                        .filter(xmlDoc -> xmlDoc.getID().equals(doc.getDocumentId()))
                        .findFirst().orElseThrow(() -> new EntityNotFoundException(""));

                ProviderDTO provider = providerClient.findByIdentificationCard(xmlDocument.getHeader().getProvider().getIdentificationCard());
                ShopDTO shop = shopClient.findByReceiverIdAndBusinessName(receiverId, xmlDocument.getHeader().getReceiver().getBusinessName());

                Header createdHeader = headerRepository.save(Header.builder()
                        .document(doc)
                        .shopId(1L)
                        .providerId(provider.id())
                        .build());

                DocumentType documentType = documentTypeRepository.findByCode(xmlDocument.getHeader().getIdDoc().getDocType())
                        .orElseThrow(() -> new EntityNotFoundException(""));

                idDocRepository.save(IdDoc.builder()
                        .invoice(xmlDocument.getHeader().getIdDoc().getInvoice())
                        .expiredAt(xmlDocument.getHeader().getIdDoc().getExpiredIn())
                        .documentType(documentType)
                        .header(createdHeader)
                        .build());

            } catch (Exception e) {
                log.error(e.toString());
            }

        });

    }

    private void callProcessDocumentsJob(MailSupplierDTO mailSupplier) {

        try {
            jobLauncher.run(
                    processDocumentJob,
                    new JobParametersBuilder()
                            .addLong("mailSupplierId", mailSupplier.id())
                            .addLong("receiverId", mailSupplier.receiverId())
                            .addDate("date", new Date())
                            .toJobParameters()
            );
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }

    }
}
