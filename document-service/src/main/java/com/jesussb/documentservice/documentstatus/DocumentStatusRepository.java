package com.jesussb.documentservice.documentstatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentStatusRepository extends JpaRepository<DocumentStatus, Long> {

    Optional<DocumentStatus> findByName(DocumentStatusName name);

}
