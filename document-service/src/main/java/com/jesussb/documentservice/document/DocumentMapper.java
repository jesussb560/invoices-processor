package com.jesussb.documentservice.document;

import com.jesussb.documentservice.document.dto.MailXmlDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    List<Document> toEntityList(List<MailXmlDocument> mailXmlDocuments);

    @Mapping(source = "ID", target = "documentId")
    @Mapping(target = "documentStatus", ignore = true)
    Document toEntity(MailXmlDocument mailXmlDocument);

}