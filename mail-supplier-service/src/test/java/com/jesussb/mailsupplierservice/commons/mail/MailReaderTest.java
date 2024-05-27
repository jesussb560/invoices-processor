package com.jesussb.mailsupplierservice.commons.mail;

import jakarta.mail.Multipart;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class MailReaderTest {

    @Test
    void getMailSupplierAttachments() throws Exception {

        Object notMultipart = new Object();
        assertThat(isMultipart(notMultipart)).isFalse();

        Multipart multipart = mock(Multipart.class);
        assertThat(isMultipart(multipart)).isTrue();

        String inputData = "Test Input Data";
        byte[] inputBytes = inputData.getBytes(StandardCharsets.UTF_8);

        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        byte[] buffer = new byte[inputBytes.length];

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        String outputData = outputStream.toString(StandardCharsets.UTF_8);
        assertThat(inputData).isEqualTo(outputData);

    }

    private boolean isMultipart(Object object) {
        return object instanceof Multipart;
    }

}