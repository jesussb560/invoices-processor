package com.jesussb.mailsupplierservice.commons.mail;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.Folder;
import jakarta.mail.Session;
import jakarta.mail.Store;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class MailReaderIntegrationTest {

    private GreenMail greenMail;

    @BeforeEach
    void setUp() {
        greenMail = new GreenMail(ServerSetupTest.IMAP);
        greenMail.start();
        greenMail.setUser("user@example.com", "password");
    }

    @AfterEach
    void tearDown() {
        greenMail.stop();
    }

    @Test
    void connection() throws Exception {

        Properties props = new Properties();
        props.put("mail.imap.host", "localhost");
        props.put("mail.imap.port", "3143");
        props.put("mail.imap.starttls.enable", "true");

        Session session = Session.getDefaultInstance(props);
        Store store = session.getStore("imap");
        store.connect("localhost", "user@example.com", "password");

        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_WRITE);

        assertThat(folder.isOpen()).isTrue();

        folder.close();
        store.close();

        assertThat(folder.isOpen()).isFalse();
        assertThat(store.isConnected()).isFalse();

    }


}