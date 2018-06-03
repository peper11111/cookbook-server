package pl.edu.pw.ee.cookbookserver.service;

import pl.edu.pw.ee.cookbookserver.entity.Token;

public interface MailService {

    void sendMessage(String to, String subject, String text);
    void sendAccountActivationMessage(String origin, Token token);
    void sendPasswordResetMessage(String origin, Token token);
}
