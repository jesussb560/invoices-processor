package com.jesussb.documentservice.document.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "Encabezado")
@Data
public class MailXmlHeader {

    @JacksonXmlProperty(localName = "IdDoc")
    private MailXmlIdDoc idDoc;

    @JacksonXmlProperty(localName = "Emisor")
    private MailXmlProvider provider;

    @JacksonXmlProperty(localName = "Receptor")
    private MailXmlReceiver receiver;

    @JacksonXmlProperty(localName = "Totales")
    private MailXmlTotal total;

}
