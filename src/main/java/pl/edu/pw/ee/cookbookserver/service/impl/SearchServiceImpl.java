package pl.edu.pw.ee.cookbookserver.service.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ee.cookbookserver.dto.SearchDto;
import pl.edu.pw.ee.cookbookserver.entity.User;
import pl.edu.pw.ee.cookbookserver.helper.PayloadHelper;
import pl.edu.pw.ee.cookbookserver.helper.UserHelper;
import pl.edu.pw.ee.cookbookserver.repository.UserRepository;
import pl.edu.pw.ee.cookbookserver.service.SearchService;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Transactional(rollbackFor = Exception.class)
public class SearchServiceImpl implements SearchService {

    private PayloadHelper payloadHelper;
    private UserHelper userHelper;
    private UserRepository userRepository;

    @Autowired
    public SearchServiceImpl(PayloadHelper payloadHelper, UserHelper userHelper, UserRepository userRepository) {
        this.payloadHelper = payloadHelper;
        this.userHelper = userHelper;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity search(JSONObject payload) throws Exception {
        String query = payloadHelper.getValidQuery(payload);

        Stream<User> stream = StreamSupport.stream(userRepository.findAll().spliterator(), false);
        stream = stream.filter(user -> user.getUsername().toLowerCase().contains(query.toLowerCase()));

        Iterable<User> users = stream.collect(Collectors.toList());
        Collection<SearchDto> searchDtos = userHelper.mapUserToSearchDto(users);
        return ResponseEntity.status(HttpStatus.OK).body(searchDtos);
    }
}
