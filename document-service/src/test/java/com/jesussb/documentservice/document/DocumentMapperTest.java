package com.jesussb.documentservice.document;

import com.jesussb.documentservice.document.dto.MailXmlDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DocumentMapperTest {

    @Spy
    private DocumentMapper documentMapper = Mappers.getMapper(DocumentMapper.class);
    private final List<MailXmlDocument> mailXmlDocuments = new ArrayList<>();

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        MailXmlDocument dte = new MailXmlDocument();
        dte.setID("F33T1234");

        MailXmlDocument dte2 = new MailXmlDocument();
        dte2.setID("F33T1231");

        mailXmlDocuments.add(dte);
        mailXmlDocuments.add(dte2);

    }

    @Test
    void toEntityList() {

        List<Document> mappedDocuments = documentMapper.toEntityList(mailXmlDocuments);

        assertThat(mappedDocuments.size()).isEqualTo(2);
        assertThat(mappedDocuments.getFirst().getDocumentId()).isEqualTo("F33T1234");
        assertThat(mappedDocuments.getLast().getDocumentId()).isEqualTo("F33T1231");

    }

    @Test
    void toEntity() {

        Document mappedDocument = documentMapper.toEntity(mailXmlDocuments.getFirst());

        assertThat(mappedDocument.getDocumentId()).isEqualTo("F33T1234");
        assertThat(mappedDocument.getDocumentStatus()).isNull();

    }
}