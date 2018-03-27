package pl.edu.pw.ee.cookbookserver.service;

public interface MailService {
    void sendMessage(String to, String subject, String text);
}
