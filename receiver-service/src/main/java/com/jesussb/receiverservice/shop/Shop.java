package com.jesussb.receiverservice.shop;

import com.jesussb.receiverservice.receiver.Receiver;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Calendar;

@Entity
@Table(name = "shops")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String businessName;
    private String address;
    private String city;

    private Boolean active;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private Receiver receiver;

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


