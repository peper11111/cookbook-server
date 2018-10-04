package pl.edu.pw.ee.cookbookserver.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ee.cookbookserver.entity.Token;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.misc.Error;
import pl.edu.pw.ee.cookbookserver.misc.ProcessingException;
import pl.edu.pw.ee.cookbookserver.misc.TokenType;
import pl.edu.pw.ee.cookbookserver.repository.TokenRepository;

import java.time.LocalDateTime;

@Component
public class TokenHelper {

    private TokenRepository tokenRepository;

    @Autowired
    public TokenHelper(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Token getToken(User user, TokenType type) throws ProcessingException {
        Iterable<Token> tokens = tokenRepository.findByUserAndType(user, type);
        for (Token token: tokens) {
            if (token.getExpirationTime().compareTo(LocalDateTime.now()) < 0) {
                continue;
            }
            return token;
        }
        throw new ProcessingException(Error.TOKEN_NOT_FOUND);
    }

    public Token getToken(String uuid, TokenType type) throws ProcessingException {
        Token token = tokenRepository.findByUuidAndType(uuid, type).orElse(null);
        if (token == null) {
            throw new ProcessingException(Error.TOKEN_NOT_FOUND);
        }
        if (token.getExpirationTime().compareTo(LocalDateTime.now()) < 0) {
            throw new ProcessingException(Error.TOKEN_EXPIRED);
        }
        return token;
    }
}
