package com.jesussb.documentservice.document.batch.writer;

import com.jesussb.documentservice.document.DocumentService;
import com.jesussb.documentservice.document.dto.MailXmlDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@StepScope
@Component
@Slf4j
public class DocumentWriter implements ItemWriter<MailXmlDocument> {

    @Value("#{jobParameters['receiverId']}")
    private Long receiverId;

    private final DocumentService documentService;

    @Override
    public void write(Chunk<? extends MailXmlDocument> chunk) throws Exception {
        List<MailXmlDocument> documents = new ArrayList<>(chunk.getItems());
        documentService.saveDocuments(documents, receiverId);
    }

}
