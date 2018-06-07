package pl.edu.pw.ee.cookbookserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pw.ee.cookbookserver.service.CuisineService;

@RestController
@RequestMapping("/cuisines")
public class CuisineController {

    private CuisineService cuisineService;

    @Autowired
    public CuisineController(CuisineService cuisineService) {
        this.cuisineService = cuisineService;
    }

    @GetMapping
    public ResponseEntity readAll() {
        return cuisineService.readAll();
    }
}
