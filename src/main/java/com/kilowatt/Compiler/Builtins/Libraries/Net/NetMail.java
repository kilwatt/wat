package com.kilowatt.Compiler.Builtins.Libraries.Net;

import com.kilowatt.Compiler.WattCompiler;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.VmAddress;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

/*
Net -> Почта
 */
public class NetMail {
    // создание сэссии
    public Session create_session(String login, String password) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.debug", "false");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(login, password);
            }
        });
    }

    // соеденение
    public Transport connect(Session session) {
        try {
            Transport transport = session.getTransport("smtp");
            transport.connect();
            return transport;
        } catch (MessagingException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "mail error: " + e.getMessage(),
                    "check your code."
            );
        }
    }

    // закрытие соеденения
    public void close(Transport transport) {
        try {
            transport.close();
        } catch (MessagingException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                    address.getLine(),
                    address.getFileName(),
                    "mail error: " + e.getMessage(),
                    "check your code."
            );
        }
    }

    // отправка письма
    public void send_mail(Session session, Transport transport, String from, String to, String subject, String text) {
        try {
            // формирование сообщения
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setSubject(subject);
            message.setText(text);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            // отправка
            transport.sendMessage(message, InternetAddress.parse(to));
        } catch (MessagingException e) {
            VmAddress address = WattCompiler.vm.getCallsHistory().getLast().getAddress();
            throw new WattRuntimeError(
                address.getLine(),
                address.getFileName(),
                "mail error: " + e.getMessage(),
                "check your code."
            );
        }
    }
}
