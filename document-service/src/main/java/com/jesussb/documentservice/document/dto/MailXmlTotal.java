package com.jesussb.documentservice.document.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MailXmlTotal{
        private Integer netAmount;
        private Integer tax;
}
