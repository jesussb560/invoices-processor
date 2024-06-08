package com.jesussb.documentservice.mailsupplier.dto;

import java.sql.Timestamp;

public record MailSupplierDTO (
    Long id,
    String mailAddress,
    String password,
    String host,
    String port,
    Boolean active,
    Timestamp createdAt,
    Timestamp updatedAt,
    Long receiverId
    ){

}
