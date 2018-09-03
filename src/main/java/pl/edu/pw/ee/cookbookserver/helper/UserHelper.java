package pl.edu.pw.ee.cookbookserver.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.repository.UserRepository;
import pl.edu.pw.ee.cookbookserver.util.Error;
import pl.edu.pw.ee.cookbookserver.util.ProcessingException;

@Component
public class UserHelper {

    private UserRepository userRepository;

    @Autowired
    public UserHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() throws ProcessingException {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findById(principal.getId()).orElse(null);
        if (currentUser == null) {
            throw new ProcessingException(Error.ACCESS_DENIED);
        }
        return currentUser;
    }
}
