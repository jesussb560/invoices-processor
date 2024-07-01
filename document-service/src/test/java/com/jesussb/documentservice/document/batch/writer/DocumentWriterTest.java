package com.jesussb.documentservice.document.batch.writer;

import com.jesussb.documentservice.document.DocumentService;
import com.jesussb.documentservice.document.dto.MailXmlDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.Chunk;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentWriterTest {

    @InjectMocks
    private DocumentWriter documentWriter;
    @Mock
    private DocumentService documentService;

    private final Long receiverId = 1L;
    private final List<MailXmlDocument> docs = new ArrayList<>();

    @BeforeEach
    void setUp() {

        ReflectionTestUtils.setField(documentWriter, "receiverId", receiverId);

        MailXmlDocument doc1 = new MailXmlDocument();
        MailXmlDocument doc2 = new MailXmlDocument();

        docs.add(doc1);
        docs.add(doc2);

    }

    @Test
    void write() throws Exception {

        Chunk<MailXmlDocument> chunk = new Chunk<>(docs);

        documentWriter.write(chunk);

        verify(documentService, times(1)).saveDocuments(docs, receiverId);

    }

    @Test
    void writeException() {

        Chunk<MailXmlDocument> chunk = new Chunk<>(docs);

        doThrow(new RuntimeException("Test exception"))
                .when(documentService).saveDocuments(docs, receiverId);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            documentWriter.write(chunk);
        });

        assertThat(exception.getMessage()).isEqualTo("Test exception");
        verify(documentService, times(1)).saveDocuments(docs, receiverId);

    }

}