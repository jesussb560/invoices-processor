package com.jesussb.mailsupplierservice.mailsupplier;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Calendar;

@Entity(name = "MailSupplier")
@Table(name = "mail_suppliers")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MailSupplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mailAddress;
    private String password;
    private String host;
    private String port;

    private Boolean active;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    private Long receiverId;

    @PrePersist
    private void prePersist() {
        this.active = true;
        this.createdAt = new Timestamp(Calendar.getInstance().getTimeInMillis());
        this.updatedAt = null;
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = new Timestamp(Calendar.getInstance().getTimeInMillis());
    }

}
