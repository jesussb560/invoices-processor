package com.jesussb.mailsupplierservice.mailsupplier;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MailSupplierRepository extends JpaRepository<MailSupplier, Long> {

    @Query("SELECT ms FROM MailSupplier ms WHERE ms.active = true")
    List<MailSupplier> findAllActive();

}
