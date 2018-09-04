package pl.edu.pw.ee.cookbookserver.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import pl.edu.pw.ee.cookbookserver.entity.Token;
import pl.edu.pw.ee.cookbookserver.repository.TokenRepository;
import pl.edu.pw.ee.cookbookserver.repository.UserRepository;
import pl.edu.pw.ee.cookbookserver.util.Error;
import pl.edu.pw.ee.cookbookserver.util.PayloadKey;
import pl.edu.pw.ee.cookbookserver.util.ProcessingException;

import java.time.LocalDateTime;

@Component
public class PayloadHelper {

    private TokenRepository tokenRepository;
    private UserRepository userRepository;

    @Autowired
    public PayloadHelper(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    public Token getValidToken(JSONObject payload) throws ProcessingException {
        String uuidKey = PayloadKey.UUID.value();
        if (!payload.has(uuidKey)) {
            throw new ProcessingException(Error.MISSING_UUID);
        }
        String uuid = payload.optString(uuidKey);
        Token token = tokenRepository.findByUuid(uuid).orElse(null);
        if (token == null) {
            throw new ProcessingException(Error.TOKEN_NOT_FOUND);
        }
        if (token.getExpirationTime().compareTo(LocalDateTime.now()) < 0) {
            throw new ProcessingException(Error.TOKEN_EXPIRED);
        }
        return token;
    }

    public String getValidEmail(JSONObject payload) throws ProcessingException {
        String emailKey = PayloadKey.EMAIL.value();
        if (!payload.has(emailKey)) {
            throw new ProcessingException(Error.MISSING_EMAIL);
        }
        String email = payload.optString(emailKey);
        if (!email.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")) {
            throw new ProcessingException(Error.INVALID_EMAIL);
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ProcessingException(Error.EMAIL_OCCUPIED);
        }
        return email;
    }

    public String getValidUsername(JSONObject payload) throws ProcessingException {
        String usernameKey = PayloadKey.USERNAME.value();
        if (!payload.has(usernameKey)) {
            throw new ProcessingException(Error.MISSING_USERNAME);
        }
        String username = payload.optString(usernameKey);
        if (username.isEmpty()) {
            throw new ProcessingException(Error.INVALID_USERNAME);
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ProcessingException(Error.USERNAME_OCCUPIED);
        }
        return username;
    }

    public String getValidPassword(JSONObject payload) throws ProcessingException {
        String passwordKey = PayloadKey.PASSWORD.value();
        if (!payload.has(passwordKey)) {
            throw new ProcessingException(Error.MISSING_PASSWORD);
        }
        String password = payload.optString(passwordKey);
        if (password.length() < 8) {
            throw new ProcessingException(Error.PASSWORD_TOO_SHORT);
        }
        return password;
    }
}
