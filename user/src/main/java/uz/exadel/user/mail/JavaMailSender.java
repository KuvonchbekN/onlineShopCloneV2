package uz.exadel.user.mail;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;


@Configuration
@ConfigurationProperties(prefix = "mail")
public class JavaMailSender {
    private String username; //TODO i should read from application.yml.prop file
    private String password;


    @Bean
    public org.springframework.mail.javamail.JavaMailSender getJavaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);


        mailSender.setUsername("kuvonchbekgodfather@gmail.com");
        mailSender.setPassword("gladnessmi6");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
