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
import com.jesussb.documentservice.provider.ProviderClient;
import com.jesussb.documentservice.provider.dto.ProviderDTO;
import com.jesussb.documentservice.shop.ShopClient;
import com.jesussb.documentservice.shop.dto.ShopDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.batch.core.launch.JobLauncher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DocumentServiceImplTest {

    @InjectMocks
    private DocumentServiceImpl documentServiceImpl;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private HeaderRepository headerRepository;

    @Mock
    private DocumentTypeRepository documentTypeRepository;

    @Mock
    private IdDocRepository idDocRepository;

    @Mock
    private MailSupplierClient mailSupplierClient;

    @Mock
    private ProviderClient providerClient;

    @Mock
    private ShopClient shopClient;

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private DocumentStatusRepository documentStatusRepository;

    @Spy
    private DocumentMapper documentMapper = Mappers.getMapper(DocumentMapper.class);

    private final List<MailXmlDocument> mailXmlDocuments = new ArrayList<>();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        MailXmlDocument m1 = new MailXmlDocument();
        m1.setID("test-id-112233");

        MailXmlDocument m2 = new MailXmlDocument();
        m2.setID("test-id-112244");

        mailXmlDocuments.add(m1);
        mailXmlDocuments.add(m2);

    }

    @Test
    void processMailDocuments() {
    }

    @Test
    void saveDocuments() {

        List<Document> mappedDocuments = documentMapper.toEntityList(mailXmlDocuments);
        DocumentStatus documentStatusTest = DocumentStatus.builder()
                .id(1L)
                .name(DocumentStatusName.PROCESSED)
                .build();

        assertThat(mappedDocuments).hasSize(2);
        assertThat(mappedDocuments.get(0).getDocumentId()).isEqualTo("test-id-112233");
        assertThat(mappedDocuments.get(1).getDocumentId()).isEqualTo("test-id-112244");

        when(documentStatusRepository.findByName(DocumentStatusName.PROCESSED)).thenReturn(Optional.of(documentStatusTest));
        assertThat(documentStatusRepository.findByName(DocumentStatusName.PROCESSED).isPresent()).isTrue();

        DocumentStatus documentStatus = documentStatusRepository.findByName(DocumentStatusName.PROCESSED).get();

        mappedDocuments.forEach(document -> {
            document.setDocumentStatus(documentStatus);
        });

        List<DocumentStatus> documentStatuses = mappedDocuments.stream().map(Document::getDocumentStatus).toList();

        assertThat(documentStatuses).hasSize(2);
        assertThat(documentStatuses.contains(null)).isFalse();

        when(documentRepository.saveAll(mappedDocuments)).thenReturn(mappedDocuments);

        var documents = documentRepository.saveAll(mappedDocuments);

        assertThat(documents).hasSize(2);

        Document firstDoc = documents.getFirst();
        Optional<MailXmlDocument> xmlDocument = mailXmlDocuments.stream().filter(doc -> doc.getID().equals("test-id-112233")).findFirst();

        assertThat(xmlDocument.isPresent()).isTrue();

        ProviderDTO providerDTO = new ProviderDTO(1L, "", "", "", "", "", "", "", "");
        when(providerClient.findByIdentificationCard("ID")).thenReturn(providerDTO);

        ShopDTO shopDTO = new ShopDTO(1L);
        when(shopClient.findByReceiverIdAndBusinessName(1L, "TEST")).thenReturn(shopDTO);

        Header headerTest = Header
                .builder()
                .id(1L)
                .document(firstDoc)
                .shopId(shopDTO.id())
                .providerId(providerDTO.id())
                .build();

        when(headerRepository.save(any(Header.class))).thenReturn(headerTest);

        ProviderDTO provider = providerClient.findByIdentificationCard("ID");
        ShopDTO shop = shopClient.findByReceiverIdAndBusinessName(1L, "TEST");

        Header savedHeader = headerRepository.save(
                Header.builder()
                        .document(firstDoc)
                        .providerId(provider.id())
                        .shopId(shop.id())
                        .build()
        );

        assertThat(provider).isNotNull();
        assertThat(shop).isNotNull();
        assertThat(savedHeader.getId()).isEqualTo(1L);
        assertThat(savedHeader.getProviderId()).isEqualTo(shop.id());
        assertThat(savedHeader.getShopId()).isEqualTo(shop.id());

        DocumentType documentType = DocumentType.builder().id(1L).build();
        IdDoc idDoc = IdDoc.builder()
                .id(1L)
                .header(savedHeader)
                .documentType(documentType)
                .build();

        when(idDocRepository.save(any(IdDoc.class))).thenReturn(idDoc);

        IdDoc savedIdDoc = idDocRepository.save(
                IdDoc.builder()
                        .header(savedHeader)
                        .documentType(documentType)
                        .build()
        );

        assertThat(savedIdDoc.getHeader().getId()).isEqualTo(1L);

    }
}