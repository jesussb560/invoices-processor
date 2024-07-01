package com.jesussb.documentservice.document.batch.processors;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.jesussb.documentservice.document.dto.MailXmlDocument;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;


@Slf4j
@RequiredArgsConstructor
public class DocumentProcessor implements ItemProcessor<String, MailXmlDocument> {

    @Override
    public MailXmlDocument process(String item) throws Exception {
        return mapXml(cleanXml(item));
    }

    private String cleanXml(String xml) {

        int start = xml.indexOf("<Documento");
        int end = xml.indexOf("</Documento>");

        return (start == -1 || end == -1) ? "" : xml
                .substring(start, end + "</Documento>".length())
                .replaceAll("\n", "")
                .replaceAll("\t", "")
                .replaceAll("\r", "")
                .replaceAll("\\\\", "");

    }

    @SneakyThrows
    private MailXmlDocument mapXml(String xml) {

        XmlMapper mapper = new XmlMapper();
        return mapper.readValue(xml, MailXmlDocument.class);

    }

}
