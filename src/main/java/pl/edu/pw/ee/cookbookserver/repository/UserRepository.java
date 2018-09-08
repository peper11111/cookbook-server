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
    Optional<User> findByUsernameOrEmail(String username, String email);

    @Query(value = "SELECT COUNT(*) FROM cb_user_followers WHERE user_id = :userId AND follower_id = :followerId", nativeQuery = true)
    long countFollowing(@Param("userId") Long userId, @Param("followerId") Long followerId);
    @Query(value = "SELECT COUNT(*) FROM cb_user_followers WHERE user_id = :id", nativeQuery = true)
    long countFollowers(@Param("id") Long id);
    @Query(value = "SELECT COUNT(*) FROM cb_user_followers WHERE follower_id = :id", nativeQuery = true)
    long countFollowed(@Param("id") Long id);

}
