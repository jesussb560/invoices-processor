package com.jesussb.documentservice.iddoc;

import com.jesussb.documentservice.documenttype.DocumentType;
import com.jesussb.documentservice.header.Header;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "id_docs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdDoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String invoice;

    private Date issuedAt;
    private Date expiredAt;

    @Column(nullable = true)
    private Date dateFrom;
    @Column(nullable = true)
    private Date dateTo;

    private Boolean active;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "header_id", nullable = false)
    private Header header;

    @ManyToOne
    @JoinColumn(name = "document_type_id", nullable = false)
    private DocumentType documentType;

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
