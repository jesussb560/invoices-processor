package com.jesussb.documentservice.header;

import com.jesussb.documentservice.document.Document;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Calendar;

@Entity @Table(name = "headers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
public class Header {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean active;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    private Long providerId;
    private Long shopId;

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
