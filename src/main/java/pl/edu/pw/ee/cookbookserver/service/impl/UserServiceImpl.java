package pl.edu.pw.ee.cookbookserver.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.edu.pw.ee.cookbookserver.dto.UserDto;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.repository.UserRepository;
import pl.edu.pw.ee.cookbookserver.service.MailService;
import pl.edu.pw.ee.cookbookserver.service.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private MailService mailService;
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(MailService mailService, UserRepository userRepository) {
        this.mailService = mailService;
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

        User user = new ModelMapper().map(userDto, User.class);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        userRepository.save(user);

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

        mailService.sendMessage(optionalUser.get().getEmail(), "Password reset", "Testing mail");
        return ResponseEntity.ok().body("info.password-reset");
    }
}
