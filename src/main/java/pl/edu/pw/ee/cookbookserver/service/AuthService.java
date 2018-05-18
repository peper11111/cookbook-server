package pl.edu.pw.ee.cookbookserver.service;

import org.springframework.http.ResponseEntity;
import pl.edu.pw.ee.cookbookserver.dto.AuthDto;

public interface AuthService {

    ResponseEntity register(AuthDto authDto);
    ResponseEntity verify(AuthDto authDto);
    ResponseEntity reset(AuthDto authDto);
    ResponseEntity confirm(AuthDto authDto);
}
