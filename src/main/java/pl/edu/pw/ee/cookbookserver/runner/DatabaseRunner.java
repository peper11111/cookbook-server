package pl.edu.pw.ee.cookbookserver.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.edu.pw.ee.cookbookserver.entity.*;
import pl.edu.pw.ee.cookbookserver.repository.*;

import java.util.Arrays;
import java.util.Collection;

@Component
public class DatabaseRunner implements CommandLineRunner {

    private CommentRepository commentRepository;
    private CuisineRepository cuisineRepository;
    private RecipeRepository recipeRepository;
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    @Autowired
    public DatabaseRunner(CommentRepository commentRepository, CuisineRepository cuisineRepository,
                          RecipeRepository recipeRepository, RoleRepository roleRepository,
                          UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.cuisineRepository = cuisineRepository;
        this.recipeRepository = recipeRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Role role1 = createRole("ROLE_ADMIN");

        User user1 = createUser("fitnut", "fitnut@example.com", null, null);
        User user2 = createUser("lalala", "lalala@example.com", null, null);
        User user3 = createUser("koko", "koko@example.com", null, Arrays.asList(user1, user2));
        User user4 = createUser("perlage", "perlage@example.com", null, null);
        User user5 = createUser("peper11111", "peper11111@example.com", Arrays.asList(role1), Arrays.asList(user3, user4));

        Cuisine cuisine1 = createCuisine("CUISINE_AMERICAN");
        Cuisine cuisine2 = createCuisine("CUISINE_ASIAN");
        Cuisine cuisine3 = createCuisine("CUISINE_CZECH");
        Cuisine cuisine4 = createCuisine("CUISINE_FRENCH");
        Cuisine cuisine5 = createCuisine("CUISINE_GREEK");
        Cuisine cuisine6 = createCuisine("CUISINE_ITALIAN");
        Cuisine cuisine7 = createCuisine("CUISINE_POLISH");
        Cuisine cuisine8 = createCuisine("CUISINE_SPANISH");

        Comment comment1 = createComment(user1, "Komentarz 1");
        Comment comment2 = createComment(user2, "Komentarz 2");
        Comment comment3 = createComment(user3, "Komentarz 3");
        Comment comment4 = createComment(user4, "Komentarz 4");
        Comment comment5 = createComment(user5, "Komentarz 5");

        Recipe recipe1 = createRecipe(user5, "Przepis 1", cuisine1, Arrays.asList(comment1, comment2, comment3));
        Recipe recipe2 = createRecipe(user5, "Przepis 2", cuisine2, Arrays.asList(comment4, comment5));
    }

    private Role createRole(String authority) {
        Role role = new Role(authority);
        roleRepository.save(role);
        return role;
    }

    private User createUser(String username, String email, Collection<Role> authorities, Collection<User> followers) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword("12345678");
        user.setName("Peper Peper");
        user.setBiography("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin nibh augue, suscipit a, scelerisque sed, lacinia in, mi. Cras vel lorem. Etiam pellentesque aliquet tellus. Phasellus pharetra nulla ac diam.");
        user.setEnabled(true);
        user.setAuthorities(authorities);
        user.setFollowers(followers);
        userRepository.save(user);
        return user;
    }

    private Cuisine createCuisine(String name) {
        Cuisine cuisine = new Cuisine(name);
        cuisineRepository.save(cuisine);
        return cuisine;
    }

    private Comment createComment(User author, String content) {
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setContent(content);
        commentRepository.save(comment);
        return comment;
    }

    private Recipe createRecipe(User author, String title, Cuisine cuisine, Collection<Comment> comments) {
        Recipe recipe = new Recipe();
        recipe.setAuthor(author);
        recipe.setTitle(title);
        recipe.setCuisine(cuisine);
        recipe.setDifficulty(3);
        recipe.setPlates(4);
        recipe.setPreparationTime(90);
        recipe.setComments(comments);
        recipeRepository.save(recipe);
        return recipe;
    }
}
