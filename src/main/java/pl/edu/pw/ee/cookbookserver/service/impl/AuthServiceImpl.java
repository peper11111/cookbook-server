package pl.edu.pw.ee.cookbookserver.service.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ee.cookbookserver.entity.Token;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.helper.PayloadHelper;
import pl.edu.pw.ee.cookbookserver.helper.TokenHelper;
import pl.edu.pw.ee.cookbookserver.helper.UserHelper;
import pl.edu.pw.ee.cookbookserver.repository.TokenRepository;
import pl.edu.pw.ee.cookbookserver.repository.UserRepository;
import pl.edu.pw.ee.cookbookserver.service.AuthService;
import pl.edu.pw.ee.cookbookserver.service.MailService;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private MailService mailService;
    private PayloadHelper payloadHelper;
    private TokenHelper tokenHelper;
    private TokenRepository tokenRepository;
    private UserHelper userHelper;
    private UserRepository userRepository;

    @Autowired
    public AuthServiceImpl(MailService mailService, PayloadHelper payloadHelper, TokenHelper tokenHelper,
                           TokenRepository tokenRepository, UserHelper userHelper, UserRepository userRepository) {
        this.mailService = mailService;
        this.payloadHelper = payloadHelper;
        this.tokenHelper = tokenHelper;
        this.tokenRepository = tokenRepository;
        this.userHelper = userHelper;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity register(JSONObject payload, String origin) throws Exception {
        User user = new User();
        user.setEmail(payloadHelper.getValidEmail(payload));
        user.setUsername(payloadHelper.getValidUsername(payload));
        user.setPassword(payloadHelper.getValidPassword(payload));
        userRepository.save(user);

        Token token = new Token(user);
        tokenRepository.save(token);

        mailService.sendAccountActivationMessage(origin, token);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity registerResend(JSONObject payload, String origin) throws Exception {
        String login = payloadHelper.getValidLogin(payload);
        User user = userHelper.getUser(login);
        Token token = tokenHelper.getToken(user);

        mailService.sendAccountActivationMessage(origin, token);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity registerVerify(JSONObject payload) throws Exception {
        String uuid = payloadHelper.getValidUuid(payload);
        Token token = tokenHelper.getToken(uuid);

        User user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        tokenRepository.delete(token);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity reset(JSONObject payload, String origin) throws Exception {
        String login = payloadHelper.getValidLogin(payload);
        User user = userHelper.getUser(login);

        Token token = new Token(user);
        tokenRepository.save(token);

        mailService.sendPasswordResetMessage(origin, token);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity resetResend(JSONObject payload, String origin) throws Exception {
        String login = payloadHelper.getValidLogin(payload);
        User user = userHelper.getUser(login);
        Token token = tokenHelper.getToken(user);

        mailService.sendPasswordResetMessage(origin, token);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity resetConfirm(JSONObject payload) throws Exception {
        String uuid = payloadHelper.getValidUuid(payload);
        Token token = tokenHelper.getToken(uuid);

        User user = token.getUser();
        user.setPassword(payloadHelper.getValidPassword(payload));
        userRepository.save(user);

        tokenRepository.delete(token);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
