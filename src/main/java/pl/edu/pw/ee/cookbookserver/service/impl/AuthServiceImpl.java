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
    public ResponseEntity register(JSONObject payload, String origin) throws JSONException {
        String emailKey = PayloadKey.EMAIL.value();
        if (!payload.has(emailKey) || payload.isNull(emailKey)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.MISSING_EMAIL.value());
        }

        String usernameKey = PayloadKey.USERNAME.value();
        if (!payload.has(usernameKey) || payload.isNull(usernameKey)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.MISSING_USERNAME.value());
        }

        String passwordKey = PayloadKey.PASSWORD.value();
        if (!payload.has(passwordKey) || payload.isNull(passwordKey)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.MISSING_PASSWORD.value());
        }

        String email = payload.getString(emailKey);
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseMessage.EMAIL_OCCUPIED.value());
        }

        String username = payload.getString(usernameKey);
        user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseMessage.USERNAME_OCCUPIED.value());
        }

        String password = payload.getString(passwordKey);
        user = new User();
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setEmail(email);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(false);
        userRepository.save(user);

        Token token = createAccessToken(user);
        mailService.sendAccountActivationMessage(origin, token);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity verify(JSONObject payload) throws JSONException {
        String tokenKey = PayloadKey.TOKEN.value();
        if (!payload.has(tokenKey) || payload.isNull(tokenKey)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.MISSING_TOKEN.value());
        }

        Token token = tokenRepository.findByUuid(payload.getString(tokenKey)).orElse(null);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (token.getExpirationTime().compareTo(LocalDateTime.now()) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.TOKEN_EXPIRED.value());
        }

        User user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        tokenRepository.delete(token);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity reset(JSONObject payload, String origin) throws JSONException {
        String usernameKey = PayloadKey.USERNAME.value();
        if (!payload.has(usernameKey) || payload.isNull(usernameKey)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.MISSING_USERNAME.value());
        }

        String username = payload.getString(usernameKey);
        User user = userRepository.findByUsernameOrEmail(username, username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Token token = createAccessToken(user);
        mailService.sendPasswordResetMessage(origin, token);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity confirm(JSONObject payload) throws JSONException {
        String tokenKey = PayloadKey.TOKEN.value();
        if (!payload.has(tokenKey) || payload.isNull(tokenKey)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.MISSING_TOKEN.value());
        }

        String passwordKey = PayloadKey.PASSWORD.value();
        if (!payload.has(passwordKey) || payload.isNull(passwordKey)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.MISSING_PASSWORD.value());
        }

        Token token = tokenRepository.findByUuid(payload.getString(tokenKey)).orElse(null);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (token.getExpirationTime().compareTo(LocalDateTime.now()) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.TOKEN_EXPIRED.value());
        }

        User user = token.getUser();
        user.setPassword(new BCryptPasswordEncoder().encode(payload.getString(passwordKey)));
        userRepository.save(user);
        tokenRepository.delete(token);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private Token createAccessToken(User user) {
        Token token = new Token();
        token.setUser(user);
        token.setUuid(UUID.randomUUID().toString());
        token.setExpirationTime(LocalDateTime.now().plusHours(1));
        tokenRepository.save(token);
        return token;
    }
}
