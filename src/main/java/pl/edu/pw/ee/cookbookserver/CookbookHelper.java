package pl.edu.pw.ee.cookbookserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.repository.UserRepository;

@Component
public class CookbookHelper {

    private UserRepository userRepository;

    @Autowired
    public CookbookHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(user.getId()).orElse(null);
    }
}
