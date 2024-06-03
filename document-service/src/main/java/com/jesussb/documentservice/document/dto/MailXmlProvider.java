package com.jesussb.documentservice.document.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MailXmlProvider {

    @JacksonXmlProperty(localName = "RUTEmisor")
    private String identificationCard;
    @JacksonXmlProperty(localName = "Telefono")
    private String phone;
    @JacksonXmlProperty(localName = "RznSoc")
    private String businessName;
    @JacksonXmlProperty(localName = "Acteco")
    private String economicAct;
    @JacksonXmlProperty(localName = "CorreoEmisor")
    private String emailAddress;
    @JacksonXmlProperty(localName = "DirOrigen")
    private String address;
    @JacksonXmlProperty(localName = "CmnaOrigen")
    private String commune;
    @JacksonXmlProperty(localName = "CiudadOrigen")
    private String City;


}
