package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.edu.pw.ee.cookbookserver.service.MailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {

    private JavaMailSender javaMailSender;
    private TemplateEngine templateEngine;

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendMessage(String to, String subject, String text) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom("no-reply@cookbook.com");
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessage);
    }

    @Override
    public void sendAccountActivationMessage(String to, String username, String token) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("accountActivationLink", "http://localhost:8080/verify?token=" + token);
        String text = templateEngine.process("AccountActivation", context);
        sendMessage(to, "Account activation", text);
    }

    @Override
    public void sendPasswordResetMessage(String to, String username, String token) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("passwordResetLink", "http://localhost:8080/confirm?token=" + token);
        String text = templateEngine.process("PasswordReset", context);
        sendMessage(to, "Password reset", text);
    }
}
