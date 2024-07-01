package com.jesussb.documentservice.document.batch.readers;

import com.jesussb.documentservice.mailsupplier.MailSupplierClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestDocumentReaderTest {

    @InjectMocks
    private RestDocumentReader restDocumentReader;
    @Mock
    private MailSupplierClient mailSupplierClient;

    private final Long mailSupplierId = 123L;

    List<String> body = Arrays.asList(
            "<DTE id=1><Info><Type>Factura</Type><Date>2024-06-07</Date><Amount>100.00</Amount></Info></DTE>",
            "<DTE id=2><Info><Type>Boleta</Type><Date>2024-06-07</Date><Amount>50.00</Amount></Info></DTE>",
            "<DTE id=3><Info><Type>Boleta</Type><Date>2024-06-07</Date><Amount>50.00</Amount></Info></DTE>",
            "<DTE id=4><Info><Type>Boleta</Type><Date>2024-06-07</Date><Amount>50.00</Amount></Info></DTE>",
            "<DTE id=5><Info><Type>Boleta</Type><Date>2024-06-07</Date><Amount>50.00</Amount></Info></DTE>",
            "<DTE id=6><Info><Type>Boleta</Type><Date>2024-06-07</Date><Amount>50.00</Amount></Info></DTE>"
    );

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(restDocumentReader, "mailSupplierId", mailSupplierId);
    }

    @Test
    void read() throws Exception {

        when(mailSupplierClient.getAttachments(mailSupplierId))
                .thenReturn(body);

        assertThat(body.getFirst()).isEqualTo(restDocumentReader.read());
        assertThat(body.get(1)).isEqualTo(restDocumentReader.read());
        assertThat(body.get(2)).isEqualTo(restDocumentReader.read());
        assertThat(body.get(3)).isEqualTo(restDocumentReader.read());
        assertThat(body.get(4)).isEqualTo(restDocumentReader.read());
        assertThat(body.getLast()).isEqualTo(restDocumentReader.read());

    }
}