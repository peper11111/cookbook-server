package pl.edu.pw.ee.cookbookserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ee.cookbookserver.repository.CuisineRepository;
import pl.edu.pw.ee.cookbookserver.service.CuisineService;

@Service
@Transactional(rollbackFor = Exception.class)
public class CuisineServiceImpl implements CuisineService {

    private CuisineRepository cuisineRepository;

    @Autowired
    public CuisineServiceImpl(CuisineRepository cuisineRepository) {
        this.cuisineRepository = cuisineRepository;
    }

    @Override
    public ResponseEntity readAll() {
        return ResponseEntity.status(HttpStatus.OK).body(cuisineRepository.findAll());
    }
}
