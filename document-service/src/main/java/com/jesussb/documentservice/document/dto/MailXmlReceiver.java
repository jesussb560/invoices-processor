package com.jesussb.documentservice.document.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MailXmlReceiver {

    @JacksonXmlProperty(localName = "RUTRecep")
    private String identificationCard;
    @JacksonXmlProperty(localName = "TelefonoRecep")
    private String phone;
    @JacksonXmlProperty(localName = "RznSocRecep")
    private String businessName;
    @JacksonXmlProperty(localName = "CorreoRecep")
    private String emailAddress;
    @JacksonXmlProperty(localName = "DirRecep")
    private String address;
    @JacksonXmlProperty(localName = "CmnaRecep")
    private String commune;
    @JacksonXmlProperty(localName = "CiudadRecep")
    private String City;

}
