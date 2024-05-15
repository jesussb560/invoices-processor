package com.jesussb.providerservice.provider;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Calendar;

@Entity @Table(name = "providers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String identificationCard;

    private String phone;

    @Column(nullable = false)
    private String businessName;

    private String economicAct;

    private String emailAddress;

    private String address;

    private String commune;

    private String City;

    private Boolean active;
    private Timestamp createdAt;
    private Timestamp updatedAt;

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
