package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ee.cookbookserver.dto.UserDto;
import pl.edu.pw.ee.cookbookserver.dto.BasicDto;
import pl.edu.pw.ee.cookbookserver.entity.Role;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.repository.UploadRepository;
import pl.edu.pw.ee.cookbookserver.repository.UserRepository;
import pl.edu.pw.ee.cookbookserver.service.UserService;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UploadRepository uploadRepository;
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UploadRepository uploadRepository, UserRepository userRepository) {
        this.uploadRepository = uploadRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity current() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BasicDto basicDto = new BasicDto();
        basicDto.setId(currentUser.getId());
        basicDto.setAuthorities(currentUser.getAuthorities().stream().map(Role::getAuthority).toArray(String[]::new));
        return ResponseEntity.status(HttpStatus.OK).body(basicDto);
    }

    @Override
    public ResponseEntity read(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = optionalUser.get();
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setDescription(user.getDescription());
        if (user.getAvatar() != null) {
            userDto.setAvatarId(user.getAvatar().getId());
        }
        if (user.getBanner() != null) {
            userDto.setBannerId(user.getBanner().getId());
        }

        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @Override
    public ResponseEntity update(Long id, UserDto userDto) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = optionalUser.get();
        user.setDescription(userDto.getDescription());
        user.setAvatar(userDto.getAvatarId() != null
                ? uploadRepository.findById(userDto.getAvatarId()).orElse(null)
                : null);
        user.setBanner(userDto.getBannerId() != null
                ? uploadRepository.findById(userDto.getBannerId()).orElse(null)
                : null);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
