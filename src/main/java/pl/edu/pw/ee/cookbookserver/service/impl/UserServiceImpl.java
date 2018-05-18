package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.pw.ee.cookbookserver.dto.UserDto;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.repository.UserRepository;
import pl.edu.pw.ee.cookbookserver.service.UploadService;
import pl.edu.pw.ee.cookbookserver.service.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UploadService uploadService;
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UploadService uploadService, UserRepository userRepository) {
        this.uploadService = uploadService;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity current() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok().body(authentication.getPrincipal());
    }

    @Override
    public ResponseEntity read(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (!optionalUser.isPresent()) {
            return ResponseEntity.badRequest().body("error.user-not-found");
        }

        User user = optionalUser.get();
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setAvatar(user.getAvatar());

        return ResponseEntity.ok().body(userDto);
    }

    @Override
    public ResponseEntity update(Long id, UserDto userDto) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (!optionalUser.isPresent()) {
            return ResponseEntity.badRequest().body("error.user-not-found");
        }

        User user = optionalUser.get();
        System.out.println(userDto);
        if (userDto.getAvatar() != null) {
            if (user.getAvatar() != null) {
                uploadService.delete(user.getAvatar());
            }
            user.setAvatar(userDto.getAvatar());
        }
        userRepository.save(user);

        return ResponseEntity.ok().body("info.user-update-successful");
    }
}
