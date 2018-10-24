package pl.edu.pw.ee.cookbookserver.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.ee.cookbookserver.entity.Recipe;
import pl.edu.pw.ee.cookbookserver.entity.User;

import java.util.Collection;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    Iterable<Recipe> findByBannerId(Long id);
    Iterable<Recipe> findByAuthorIn(Collection<User> authors);
    long countByAuthor(User author);
}
