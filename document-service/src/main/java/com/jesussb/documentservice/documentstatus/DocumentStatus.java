package com.jesussb.documentservice.documentstatus;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.sql.Timestamp;
import java.util.Calendar;

@Entity
@Table(name = "document_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    private DocumentStatusName name;

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
