package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ee.cookbookserver.entity.Token;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.repository.TokenRepository;
import pl.edu.pw.ee.cookbookserver.repository.UserRepository;
import pl.edu.pw.ee.cookbookserver.service.AuthService;
import pl.edu.pw.ee.cookbookserver.service.MailService;
import pl.edu.pw.ee.cookbookserver.util.Error;
import pl.edu.pw.ee.cookbookserver.util.ProcessingException;
import pl.edu.pw.ee.cookbookserver.util.PayloadKey;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private MailService mailService;
    private TokenRepository tokenRepository;
    private UserRepository userRepository;

    @Autowired
    public AuthServiceImpl(MailService mailService, TokenRepository tokenRepository, UserRepository userRepository) {
        this.mailService = mailService;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity register(JSONObject payload, String origin) throws Exception {
        String email = getValidEmail(payload);
        String username = getValidUsername(payload);
        String password = getValidPassword(payload);

        User user = new User();
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setEmail(email);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(false);
        userRepository.save(user);

        Token token = createToken(user);
        mailService.sendAccountActivationMessage(origin, token);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity verify(JSONObject payload) throws Exception {
        Token token = getValidToken(payload);

        User user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        tokenRepository.delete(token);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity reset(JSONObject payload, String origin) throws Exception {
        User user = getValidUser(payload);

        Token token = createToken(user);
        mailService.sendPasswordResetMessage(origin, token);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity confirm(JSONObject payload) throws Exception {
        Token token = getValidToken(payload);
        String password = getValidPassword(payload);

        User user = token.getUser();
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        userRepository.save(user);
        tokenRepository.delete(token);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private String getValidEmail(JSONObject payload) throws JSONException, ProcessingException {
        String emailKey = PayloadKey.EMAIL.value();
        System.out.println(emailKey + " " + payload.toString());
        if (!payload.has(emailKey) || payload.isNull(emailKey) || payload.getString(emailKey).isEmpty()) {
            throw new ProcessingException(Error.MISSING_EMAIL);
        }
        String email = payload.getString(emailKey);
        if (!email.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")) {
            throw new ProcessingException(Error.INVALID_EMAIL);
        }
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            throw new ProcessingException(Error.EMAIL_OCCUPIED);
        }
        return email;
    }

    private String getValidUsername(JSONObject payload) throws JSONException, ProcessingException {
        String usernameKey = PayloadKey.USERNAME.value();
        if (!payload.has(usernameKey) || payload.isNull(usernameKey) || payload.getString(usernameKey).isEmpty()) {
            throw new ProcessingException(Error.MISSING_USERNAME);
        }
        String username = payload.getString(usernameKey);
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            throw new ProcessingException(Error.USERNAME_OCCUPIED);
        }
        return username;
    }

    private String getValidPassword(JSONObject payload) throws JSONException, ProcessingException {
        String passwordKey = PayloadKey.PASSWORD.value();
        if (!payload.has(passwordKey) || payload.isNull(passwordKey) || payload.getString(passwordKey).isEmpty()) {
            throw new ProcessingException(Error.MISSING_PASSWORD);
        }
        String password = payload.getString(passwordKey);
        if (password.length() < 8) {
            throw new ProcessingException(Error.PASSWORD_TOO_SHORT);
        }
        return password;
    }

    private User getValidUser(JSONObject payload) throws JSONException, ProcessingException {
        String usernameKey = PayloadKey.USERNAME.value();
        if (!payload.has(usernameKey) || payload.isNull(usernameKey) || payload.getString(usernameKey).isEmpty()) {
            throw new ProcessingException(Error.MISSING_USERNAME);
        }
        String username = payload.getString(usernameKey);
        User user = userRepository.findByUsernameOrEmail(username, username).orElse(null);
        if (user == null) {
            throw new ProcessingException(Error.USER_NOT_FOUND);
        }
        return user;
    }

    private Token getValidToken(JSONObject payload) throws JSONException, ProcessingException {
        String uuidKey = PayloadKey.UUID.value();
        if (!payload.has(uuidKey) || payload.isNull(uuidKey) || payload.getString(uuidKey).isEmpty()) {
            throw new ProcessingException(Error.MISSING_UUID);
        }
        String uuid = payload.getString(uuidKey);
        Token token = tokenRepository.findByUuid(uuid).orElse(null);
        if (token == null) {
            throw new ProcessingException(Error.TOKEN_NOT_FOUND);
        }
        if (token.getExpirationTime().compareTo(LocalDateTime.now()) < 0) {
            throw new ProcessingException(Error.TOKEN_EXPIRED);
        }
        return token;
    }

    private Token createToken(User user) {
        Token token = new Token();
        token.setUser(user);
        token.setUuid(UUID.randomUUID().toString());
        token.setExpirationTime(LocalDateTime.now().plusHours(1));
        tokenRepository.save(token);
        return token;
    }
}
