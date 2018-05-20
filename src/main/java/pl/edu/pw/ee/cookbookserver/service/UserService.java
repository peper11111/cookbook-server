package pl.edu.pw.ee.cookbookserver.service;

import org.springframework.http.ResponseEntity;
import pl.edu.pw.ee.cookbookserver.dto.DetailsDto;

public interface UserService {

    ResponseEntity current();
    ResponseEntity read(Long id);
    ResponseEntity update(Long id, DetailsDto detailsDto);
}
