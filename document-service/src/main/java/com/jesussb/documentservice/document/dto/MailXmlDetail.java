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
    private int quantity;
    @JacksonXmlProperty(localName = "UnmdItem")
    private String unit;
    @JacksonXmlProperty(localName = "PrcItem")
    private int price;
    @JacksonXmlProperty(localName = "MontoItem")
    private int amout;

}
