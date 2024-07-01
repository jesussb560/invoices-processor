package com.jesussb.documentservice.document;

import com.jesussb.documentservice.document.dto.MailXmlDocument;

import java.util.List;

public interface DocumentService {

    String processMailDocuments();

    void saveDocuments(List<MailXmlDocument> xmlDocuments, Long receiverId);

}
