package com.jesussb.mailsupplierservice.commons.mail;

import com.jesussb.mailsupplierservice.mailsupplier.MailSupplier;
import jakarta.mail.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Slf4j
public class MailReader {
    @SneakyThrows
    public List<String> getMailSupplierAttachments(MailSupplier mailSupplier) {

        Properties props = new Properties();
        props.put("mail.imap.host", mailSupplier.getHost());
        props.put("mail.imap.port", mailSupplier.getPort());
        props.put("mail.imap.starttls.enable", "true");

        Session session = Session.getDefaultInstance(props);

        Store store = session.getStore("imap");
        store.connect(mailSupplier.getHost(), mailSupplier.getMailAddress(), mailSupplier.getPassword());

        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_WRITE);

        List<String> attachments = Arrays.stream(folder.getMessages(1, 100))
                .filter(this::isMultipart)
                .map(message -> {

                    List<String> attachmentsContent = new ArrayList<>();

                    try {

                        Multipart multipart = (Multipart) message.getContent();

                        for (int i = 0; i < multipart.getCount(); i++) {

                            BodyPart part = multipart.getBodyPart(i);
                            if (!Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()) || !part.getFileName().endsWith(".xml")) {
                                continue;
                            }

                            InputStream inputStream = part.getInputStream();
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            byte[] buffer = new byte[1024];

                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }

                            attachmentsContent.add(outputStream.toString(StandardCharsets.UTF_8));

                        }

                    } catch (Exception e) {
                        log.error(e.toString());
                    }

                    return attachmentsContent;

                }).flatMap(Collection::stream).toList();

        folder.close();
        store.close();

        return attachments;

    }

    private boolean isMultipart(Message message) {
        try {
            return message.getContent() instanceof Multipart;
        } catch (Exception e) {
            log.error(e.toString());
            return false;
        }
    }

}
