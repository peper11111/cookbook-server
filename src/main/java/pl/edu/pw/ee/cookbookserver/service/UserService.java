package pl.edu.pw.ee.cookbookserver.service;

import org.springframework.http.ResponseEntity;
import pl.edu.pw.ee.cookbookserver.dto.DetailsDto;

public interface UserService {

    ResponseEntity current();
    ResponseEntity readDetails(Long id);
    ResponseEntity updateDetails(Long id, DetailsDto detailsDto);
}
