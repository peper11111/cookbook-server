package pl.edu.pw.ee.cookbookserver.service;

public interface MailService {
    void sendMessage(String to, String subject, String text);
    void sendAccountActivationMessage(String to, String username, String token);
    void sendPasswordResetMessage(String to, String username, String token);
}
