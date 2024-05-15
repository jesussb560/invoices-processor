package com.jesussb.receiverservice.receiver;

import com.jesussb.receiverservice.shop.Shop;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@Entity @Table(name = "receivers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Receiver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identificationCard;
    private String businessName;
    private String phoneNumber;
    private String emailAddress;

    private Boolean active;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<Shop> shops;

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
