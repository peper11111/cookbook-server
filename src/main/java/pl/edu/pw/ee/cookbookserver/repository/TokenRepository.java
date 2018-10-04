package pl.edu.pw.ee.cookbookserver.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.ee.cookbookserver.entity.Token;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.misc.TokenType;

import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<Token, Long> {

    Iterable<Token> findByUserAndType(User user, TokenType type);
    Optional<Token> findByUuidAndType(String uuid, TokenType type);
}
