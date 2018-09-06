package pl.edu.pw.ee.cookbookserver.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.ee.cookbookserver.entity.Recipe;
import pl.edu.pw.ee.cookbookserver.entity.User;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    Iterable<Recipe> findByAuthor(User author);
    long countByAuthor(User author);
}
