package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ee.cookbookserver.dto.AuthDto;
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
    public ResponseEntity register(AuthDto authDto, String origin) {
        if (authDto.getEmail() == null || authDto.getEmail().length() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error.missing-email");
        }
        if (authDto.getUsername() == null || authDto.getUsername().length() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error.missing-username");
        }
        if (authDto.getPassword() == null || authDto.getPassword().length() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error.missing-password");
        }

        User user = userRepository.findByEmail(authDto.getEmail()).orElse(null);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("error.email-occupied");
        }
        user = userRepository.findByUsername(authDto.getUsername()).orElse(null);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("error.username-occupied");
        }

        user = new User();
        user.setUsername(authDto.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(authDto.getPassword()));
        user.setEmail(authDto.getEmail());
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
    public ResponseEntity verify(AuthDto authDto) {
        if (authDto.getToken() == null || authDto.getToken().length() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error.missing-token");
        }

        Token token = tokenRepository.findByUuid(authDto.getToken()).orElse(null);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (token.getExpirationTime().compareTo(LocalDateTime.now()) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error.token-expired");
        }

        User user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        tokenRepository.delete(token);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity reset(AuthDto authDto, String origin) {
        if (authDto.getUsername() == null || authDto.getUsername().length() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error.missing-username");
        }

        User user = userRepository.findByUsernameOrEmail(authDto.getUsername(), authDto.getUsername()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Token token = createAccessToken(user);
        mailService.sendPasswordResetMessage(origin, token);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity confirm(AuthDto authDto) {
        if (authDto.getToken() == null || authDto.getToken().length() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error.missing-token");
        }
        if (authDto.getPassword() == null || authDto.getPassword().length() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error.missing-password");
        }

        Token token = tokenRepository.findByUuid(authDto.getToken()).orElse(null);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (token.getExpirationTime().compareTo(LocalDateTime.now()) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error.token-expired");
        }

        User user = token.getUser();
        user.setPassword(new BCryptPasswordEncoder().encode(authDto.getPassword()));
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
