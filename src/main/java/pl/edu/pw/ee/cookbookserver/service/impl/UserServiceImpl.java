package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pw.ee.cookbookserver.dto.UserDto;
import pl.edu.pw.ee.cookbookserver.entity.Token;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.repository.TokenRepository;
import pl.edu.pw.ee.cookbookserver.repository.UserRepository;
import pl.edu.pw.ee.cookbookserver.service.MailService;
import pl.edu.pw.ee.cookbookserver.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private MailService mailService;
    private TokenRepository tokenRepository;
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(MailService mailService, TokenRepository tokenRepository, UserRepository userRepository) {
        this.mailService = mailService;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity register(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().length() == 0) {
            return ResponseEntity.badRequest().body("error.missing-email");
        }
        if (userDto.getUsername() == null || userDto.getUsername().length() == 0) {
            return ResponseEntity.badRequest().body("error.missing-username");
        }
        if (userDto.getPassword() == null || userDto.getPassword().length() == 0) {
            return ResponseEntity.badRequest().body("error.missing-password");
        }

        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
        if (optionalUser.isPresent()) {
            return ResponseEntity.badRequest().body("error.email-taken");
        }
        optionalUser = userRepository.findByUsername(userDto.getUsername());
        if (optionalUser.isPresent()) {
            return ResponseEntity.badRequest().body("error.username-taken");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(false);
        userRepository.save(user);

        String accessToken = createAccessToken(user);
        mailService.sendAccountActivationMessage(user.getEmail(), user.getUsername(), accessToken);

        return ResponseEntity.ok().body("info.user-created");
    }

    @Override
    public ResponseEntity reset(UserDto userDto) {
        if (userDto.getUsername() == null || userDto.getUsername().length() == 0) {
            return ResponseEntity.badRequest().body("error.missing-username");
        }

        Optional<User> optionalUser = userRepository.findByUsernameOrEmail(userDto.getUsername(), userDto.getUsername());
        if (!optionalUser.isPresent()) {
            return ResponseEntity.badRequest().body("error.user-not-found");
        }

        User user = optionalUser.get();
        String accessToken = createAccessToken(user);
        mailService.sendPasswordResetMessage(user.getEmail(), user.getUsername(), accessToken);

        return ResponseEntity.ok().body("info.password-reset");
    }

    private String createAccessToken(User user) {
        Token token = new Token();
        token.setUser(user);
        token.setUuid(UUID.randomUUID());
        token.setExpirationTime(LocalDateTime.now().plusHours(1));
        tokenRepository.save(token);

        return token.getUuid().toString();
    }
}
