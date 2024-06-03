package com.jesussb.documentservice.document.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "IdDoc")
@Data
public class MailXmlIdDoc {

    @JacksonXmlProperty(localName = "TipoDTE")
    private Integer docType;
    @JacksonXmlProperty(localName = "Folio")
    private String invoice;
    @JacksonXmlProperty(localName = "FchEmis")
    private Date issuedAt;
    @JacksonXmlProperty(localName = "FchVenc")
    private Date expiredIn;

}
