package pl.edu.pw.ee.cookbookserver.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import pl.edu.pw.ee.cookbookserver.entity.Token;
import pl.edu.pw.ee.cookbookserver.entity.Upload;
import pl.edu.pw.ee.cookbookserver.repository.TokenRepository;
import pl.edu.pw.ee.cookbookserver.repository.UploadRepository;
import pl.edu.pw.ee.cookbookserver.repository.UserRepository;
import pl.edu.pw.ee.cookbookserver.util.Error;
import pl.edu.pw.ee.cookbookserver.util.PayloadKey;
import pl.edu.pw.ee.cookbookserver.util.ProcessingException;

import java.time.LocalDateTime;

@Component
public class PayloadHelper {

    private TokenRepository tokenRepository;
    private UploadRepository uploadRepository;
    private UserRepository userRepository;

    @Autowired
    public PayloadHelper(TokenRepository tokenRepository, UploadRepository uploadRepository,
                         UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.uploadRepository = uploadRepository;
        this.userRepository = userRepository;
    }

    public String getValidString(JSONObject payload, PayloadKey payloadKey, Error error) throws ProcessingException {
        String key = payloadKey.value();
        if (!payload.has(key)) {
            throw new ProcessingException(error);
        }
        return payload.optString(key);
    }

    public long getValidLong(JSONObject payload, PayloadKey payloadKey, Error error) throws ProcessingException {
        String key = payloadKey.value();
        if (!payload.has(key)) {
            throw new ProcessingException(error);
        }
        return payload.optLong(key);
    }

    public Token getValidToken(JSONObject payload) throws ProcessingException {
        String uuid = getValidString(payload, PayloadKey.UUID, Error.MISSING_UUID);
        Token token = tokenRepository.findByUuid(uuid).orElse(null);
        if (token == null) {
            throw new ProcessingException(Error.TOKEN_NOT_FOUND);
        }
        if (token.getExpirationTime().compareTo(LocalDateTime.now()) < 0) {
            throw new ProcessingException(Error.TOKEN_EXPIRED);
        }
        return token;
    }

    public String getValidEmail(JSONObject payload) throws ProcessingException {
        String email = getValidString(payload, PayloadKey.EMAIL, Error.MISSING_EMAIL);
        if (!email.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")) {
            throw new ProcessingException(Error.INVALID_EMAIL);
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ProcessingException(Error.EMAIL_OCCUPIED);
        }
        return email;
    }

    public String getValidUsername(JSONObject payload) throws ProcessingException {
        String username = getValidString(payload, PayloadKey.USERNAME, Error.MISSING_USERNAME);
        if (username.isEmpty()) {
            throw new ProcessingException(Error.INVALID_USERNAME);
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ProcessingException(Error.USERNAME_OCCUPIED);
        }
        return username;
    }

    public String getValidPassword(JSONObject payload) throws ProcessingException {
        String password = getValidString(payload, PayloadKey.PASSWORD, Error.MISSING_PASSWORD);
        if (password.length() < 8) {
            throw new ProcessingException(Error.PASSWORD_TOO_SHORT);
        }
        return password;
    }

    public Upload getValidAvatar(JSONObject payload) throws ProcessingException {
        long avatarId = getValidLong(payload, PayloadKey.AVATAR_ID, Error.MISSING_AVATAR_ID);
        Upload avatar = uploadRepository.findById(avatarId).orElse(null);
        if (avatarId != 0 && avatar == null) {
            throw new ProcessingException(Error.AVATAR_NOT_FOUND);
        }
        return avatar;
    }

    public Upload getValidBanner(JSONObject payload) throws ProcessingException {
        long bannerId = getValidLong(payload, PayloadKey.BANNER_ID, Error.MISSING_BANNER_ID);
        Upload banner = uploadRepository.findById(bannerId).orElse(null);
        if (bannerId != 0 && banner == null) {
            throw new ProcessingException(Error.BANNER_NOT_FOUND);
        }
        return banner;
    }
}
