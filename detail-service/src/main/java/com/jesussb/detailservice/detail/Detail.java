package com.jesussb.detailservice.detail;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Calendar;

@Entity
@Table(name = "details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Detail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private int quantity;
    private String unit;
    private int price;
    private int amount;

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
