package com.jesussb.documentservice.document.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "IdDoc")
@Data
public class MailXmlDetail {

    @JacksonXmlProperty(localName = "NmbItem")
    private String name;
    @JacksonXmlProperty(localName = "DescItem")
    private String description;
    @JacksonXmlProperty(localName = "QtyItem")
    private double quantity;
    @JacksonXmlProperty(localName = "UnmdItem")
    private String unit;
    @JacksonXmlProperty(localName = "PrcItem")
    private double price;
    @JacksonXmlProperty(localName = "MontoItem")
    private double amout;

}
