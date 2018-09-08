package pl.edu.pw.ee.cookbookserver.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.ee.cookbookserver.entity.Comment;
import pl.edu.pw.ee.cookbookserver.entity.Recipe;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

    Iterable<Comment> findByRecipeAndParentIsNull(Recipe recipe);
    long countByRecipe(Recipe recipe);
}
