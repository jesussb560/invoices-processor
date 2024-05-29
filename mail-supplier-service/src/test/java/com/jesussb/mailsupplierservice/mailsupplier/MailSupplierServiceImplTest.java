package com.jesussb.mailsupplierservice.mailsupplier;

import com.jesussb.mailsupplierservice.commons.mail.MailReader;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class MailSupplierServiceImplTest {

    @InjectMocks
    private MailSupplierServiceImpl mailSupplierServiceImpl;

    @Mock
    private MailSupplierRepository mailSupplierRepository;
    @Mock
    private MailReader mailReader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAttachments() {

        MailSupplier testedMailSup = MailSupplier.builder().build();

        when(mailSupplierRepository.findById(1L)).thenReturn(Optional.of(testedMailSup));

        MailSupplier mailSupplier = mailSupplierRepository.findById(1L).orElse(null);
        assertThat(mailSupplier).isNotNull();

        when(mailReader.getMailSupplierAttachments(mailSupplier)).thenReturn(List.of("<Document></Document>", "<Document></Document>"));
        assertThat(mailReader.getMailSupplierAttachments(mailSupplier)).hasSizeGreaterThanOrEqualTo(0)
                .containsExactly("<Document></Document>", "<Document></Document>");


    }

    @Test
    void getAttachmentsEntityException() {

        when(mailSupplierRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> mailSupplierServiceImpl.getAttachments(1L));

    }

}