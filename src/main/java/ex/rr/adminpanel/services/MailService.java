package ex.rr.adminpanel.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailService {

    private final MailSender mailSender;

    public void sendSimpleMessage(){
        SimpleMailMessage message = new SimpleMailMessage();

    }

}
