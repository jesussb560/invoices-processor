package com.jesussb.documentservice.document.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

@JacksonXmlRootElement(localName = "Documento")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MailXmlDocument {

    @JacksonXmlProperty(isAttribute = true)
    private String ID;

    @JacksonXmlProperty(localName = "Encabezado")
    private MailXmlHeader header;

    @JacksonXmlProperty(localName = "Detalle")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<MailXmlDetail> details;

}
