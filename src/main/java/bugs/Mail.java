package bugs;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class Mail {
    private static Random r = new Random();

    private static void sendMail(String recipientMail, String title, String text) {
        //Declare variable
        String from = "bugslifeforever4@gmail.com";
        final String username = "bugslifeforever4@gmail.com";
        final String password = "bugslife123";
        String host = "smtp.gmail.com";

        //Default setting
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.connectiontimeout", 1000*60);
        props.put("mail.smtp.timeout", 1000*60);
        props.put("mail.smtp.writetimeout", 1000*60);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from, "Bugs Life"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientMail));
            message.setSubject(title);
            message.setText(text);
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String authorization(String recipientMail) {
        String title = "Welcome to bugs life. Here is your verification code.";
        String code =  String.format("%04d", r.nextInt(10000));
        String text = "Your verification code is " + code + ".\n\n" +
                "If you have not requested a verification code, someone else may be trying to access the Bugs Life account at \"+recipientMail+\". Please do not forward or give this verification code to anyone.";
        try {
            sendMail(recipientMail, title, text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    public static String resetPassword(String recipientMail) {
        String title = "Bugs Life verification code.";
        String code =  String.format("%04d", r.nextInt(10000));
        String text = "We received a letter from your email address requesting password reset to your Bugs Life account ("+recipientMail+"). The following is your verification code.\n\n" +
                ""+code+"\n\n" +
                "If you have not requested a verification code, someone else may be trying to access the Bugs Life account at "+recipientMail+". Please do not forward or give this verification code to anyone.";
        try {
            sendMail(recipientMail, title, text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }
}
