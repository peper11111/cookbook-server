package pl.edu.pw.ee.cookbookserver.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.pw.ee.cookbookserver.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    long countByFollowersContaining(User user);

    @Query(value = "SELECT * FROM cb_user WHERE username = :login OR email = :login", nativeQuery = true)
    Optional<User> findByLogin(@Param("login") String login);
}
