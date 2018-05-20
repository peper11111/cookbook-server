package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ee.cookbookserver.dto.DetailsDto;
import pl.edu.pw.ee.cookbookserver.dto.UserDto;
import pl.edu.pw.ee.cookbookserver.entity.Details;
import pl.edu.pw.ee.cookbookserver.entity.Upload;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.repository.DetailsRepository;
import pl.edu.pw.ee.cookbookserver.repository.UploadRepository;
import pl.edu.pw.ee.cookbookserver.repository.UserRepository;
import pl.edu.pw.ee.cookbookserver.service.UserService;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private DetailsRepository detailsRepository;
    private UploadRepository uploadRepository;
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(DetailsRepository detailsRepository, UploadRepository uploadRepository, UserRepository userRepository) {
        this.detailsRepository = detailsRepository;
        this.uploadRepository = uploadRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity current() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @Override
    public ResponseEntity read(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Details details = optionalUser.get().getDetails();
        DetailsDto detailsDto = new DetailsDto();
        detailsDto.setName(details.getName());
        detailsDto.setDescription(details.getDescription());
        if (details.getAvatar() != null) {
            detailsDto.setAvatarId(details.getAvatar().getId());
        }
        if (details.getBanner() != null) {
            detailsDto.setBannerId(details.getBanner().getId());
        }

        return ResponseEntity.status(HttpStatus.OK).body(detailsDto);
    }

    @Override
    public ResponseEntity update(Long id, DetailsDto detailsDto) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Details details = optionalUser.get().getDetails();

        if (detailsDto.getName() != null) {
            details.setName(details.getName());
        }
        if (detailsDto.getDescription() != null) {
            details.setDescription(detailsDto.getDescription());
        }
        if (detailsDto.getAvatarId() != null) {
            Optional<Upload> optionalUpload = uploadRepository.findById(detailsDto.getAvatarId());
            details.setAvatar(optionalUpload.orElse(null));
        }
        if (detailsDto.getBannerId() != null) {
            Optional<Upload> optionalUpload = uploadRepository.findById(detailsDto.getBannerId());
            details.setBanner(optionalUpload.orElse(null));
        }
        detailsRepository.save(details);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
