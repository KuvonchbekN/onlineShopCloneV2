package uz.exadel.user.mail;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl{

    @Autowired
    private final JavaMailSender emailSender;

    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }


    public void sendSimpleMessage(
        String to, String subject, String text
    ){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kuvonchbekgodfather@gmail.com");
        message.setTo(to);
        message.setText(text);
        message.setSubject(subject);

        emailSender.send(message);
    }
}
