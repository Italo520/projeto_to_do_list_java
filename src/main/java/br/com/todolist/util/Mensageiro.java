package br.com.todolist.util;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

public class Mensageiro {

    private static final String USERNAME = "ads.ifpb.testes@gmail.com";
    private static final String PASSWORD = "bjjgvzasdhjieabu";
    private static final String HOST = "smtp.gmail.com";
    private static final String PORT = "587";

    private final Session session;

    public Mensageiro() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);

        this.session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
    }

    public boolean enviarEmail(String emailDestino, String assunto, String corpoMensagem) { // Sem arquivo, só a mensagem!
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestino));
            message.setSubject(assunto);
            message.setText(corpoMensagem);
            Transport.send(message);
            System.out.println("Email enviado para " + emailDestino + " com sucesso!");
            return true;
        } catch (MessagingException e) {
            System.err.println("Erro ao enviar o email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean enviarEmailComAnexo(String emailDestino, String assunto, String corpoMensagem, String caminhoArquivo) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestino));
            message.setSubject(assunto);

            // Parte 1: O texto do e-mail
            MimeBodyPart textoBodyPart = new MimeBodyPart();
            textoBodyPart.setText(corpoMensagem);

            // Parte 2: O anexo
            MimeBodyPart anexoBodyPart = new MimeBodyPart();
            File arquivoAnexo = new File(caminhoArquivo);
            
            if (arquivoAnexo.exists() && arquivoAnexo.isFile()) {
                DataSource source = new FileDataSource(arquivoAnexo);
                anexoBodyPart.setDataHandler(new DataHandler(source));
                anexoBodyPart.setFileName(arquivoAnexo.getName());
            } else {
                System.err.println("Aviso: O arquivo '" + caminhoArquivo + "' não foi encontrado. E-mail não será enviado.");
                return false;
            }

            // Cria o corpo da mensagem com as duas partes
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textoBodyPart);
            multipart.addBodyPart(anexoBodyPart);
            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Email com anexo enviado para " + emailDestino + " com sucesso!");
            return true;

        } catch (MessagingException e) {
            System.err.println("Erro ao enviar o email com anexo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}