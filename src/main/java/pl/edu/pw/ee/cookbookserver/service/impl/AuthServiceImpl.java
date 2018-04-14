package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pw.ee.cookbookserver.dto.MessageDto;
import pl.edu.pw.ee.cookbookserver.dto.AuthDto;
import pl.edu.pw.ee.cookbookserver.entity.Token;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.repository.TokenRepository;
import pl.edu.pw.ee.cookbookserver.repository.UserRepository;
import pl.edu.pw.ee.cookbookserver.service.AuthService;
import pl.edu.pw.ee.cookbookserver.service.MailService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
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
    public ResponseEntity register(AuthDto authDto) {
        MessageDto messageDto = new MessageDto();
        if (authDto.getEmail() == null || authDto.getEmail().length() == 0) {
            messageDto.setMessage("error.missing-email");
            return ResponseEntity.badRequest().body(messageDto);
        }
        if (authDto.getUsername() == null || authDto.getUsername().length() == 0) {
            messageDto.setMessage("error.missing-username");
            return ResponseEntity.badRequest().body(messageDto);
        }
        if (authDto.getPassword() == null || authDto.getPassword().length() == 0) {
            messageDto.setMessage("error.missing-password");
            return ResponseEntity.badRequest().body(messageDto);
        }

        Optional<User> optionalUser;
        optionalUser = userRepository.findByEmail(authDto.getEmail());
        if (optionalUser.isPresent()) {
            messageDto.setMessage("error.email-occupied");
            return ResponseEntity.badRequest().body(messageDto);
        }
        optionalUser = userRepository.findByUsername(authDto.getUsername());
        if (optionalUser.isPresent()) {
            messageDto.setMessage("error.username-occupied");
            return ResponseEntity.badRequest().body(messageDto);
        }

        User user = new User();
        user.setUsername(authDto.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(authDto.getPassword()));
        user.setEmail(authDto.getEmail());
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(false);
        userRepository.save(user);

        Token token = createAccessToken(user);
        mailService.sendAccountActivationMessage(user.getEmail(), user.getUsername(), token.getUuid());

        messageDto.setMessage("info.account-activation-email-sent");
        return ResponseEntity.ok().body(messageDto);
    }

    @Override
    public ResponseEntity verify(AuthDto authDto) {
        MessageDto messageDto = new MessageDto();
        if (authDto.getToken() == null || authDto.getToken().length() == 0) {
            messageDto.setMessage("error.missing-token");
            return ResponseEntity.badRequest().body(messageDto);
        }

        Optional<Token> optionalToken;
        optionalToken = tokenRepository.findByUuid(authDto.getToken());
        if (!optionalToken.isPresent()) {
            messageDto.setMessage("error.token-not-found");
            return ResponseEntity.badRequest().body(messageDto);
        }

        Token token = optionalToken.get();
        if (token.getExpirationTime().compareTo(LocalDateTime.now()) < 0) {
            messageDto.setMessage("error.token-expired");
            return ResponseEntity.badRequest().body(messageDto);
        }

        User user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        tokenRepository.delete(token);

        messageDto.setMessage("info.user-verified");
        return ResponseEntity.ok().body(messageDto);
    }

    @Override
    public ResponseEntity reset(AuthDto authDto) {
        MessageDto messageDto = new MessageDto();
        if (authDto.getUsername() == null || authDto.getUsername().length() == 0) {
            messageDto.setMessage("error.missing-username");
            return ResponseEntity.badRequest().body(messageDto);
        }

        Optional<User> optionalUser = userRepository.findByUsernameOrEmail(authDto.getUsername(), authDto.getUsername());
        if (!optionalUser.isPresent()) {
            messageDto.setMessage("error.user-not-found");
            return ResponseEntity.badRequest().body(messageDto);
        }

        User user = optionalUser.get();
        Token token = createAccessToken(user);
        mailService.sendPasswordResetMessage(user.getEmail(), user.getUsername(), token.getUuid());

        messageDto.setMessage("info.password-reset-email-sent");
        return ResponseEntity.ok().body(messageDto);
    }

    @Override
    public ResponseEntity confirm(AuthDto authDto) {
        MessageDto messageDto = new MessageDto();
        if (authDto.getToken() == null || authDto.getToken().length() == 0) {
            messageDto.setMessage("error.missing-token");
            return ResponseEntity.badRequest().body(messageDto);
        }
        if (authDto.getPassword() == null || authDto.getPassword().length() == 0) {
            messageDto.setMessage("error.missing-password");
            return ResponseEntity.badRequest().body(messageDto);
        }

        Optional<Token> optionalToken;
        optionalToken = tokenRepository.findByUuid(authDto.getToken());
        if (!optionalToken.isPresent()) {
            messageDto.setMessage("error.token-not-found");
            return ResponseEntity.badRequest().body(messageDto);
        }

        Token token = optionalToken.get();
        if (token.getExpirationTime().compareTo(LocalDateTime.now()) < 0) {
            messageDto.setMessage("error.token-expired");
            return ResponseEntity.badRequest().body(messageDto);
        }

        User user = token.getUser();
        user.setPassword(new BCryptPasswordEncoder().encode(authDto.getPassword()));
        userRepository.save(user);
        tokenRepository.delete(token);

        messageDto.setMessage("info.password-reset");
        return ResponseEntity.ok().body(messageDto);
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
