package pl.edu.pw.ee.cookbookserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import pl.edu.pw.ee.cookbookserver.entity.*;
import pl.edu.pw.ee.cookbookserver.repository.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@Slf4j
@Component
public class Lifecycle {

    private CategoryRepository categoryRepository;
    private CommentRepository commentRepository;
    private CuisineRepository cuisineRepository;
    private Properties properties;
    private RecipeRepository recipeRepository;
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    @Autowired
    public Lifecycle(CategoryRepository categoryRepository, CommentRepository commentRepository,
                     CuisineRepository cuisineRepository, Properties properties, RecipeRepository recipeRepository,
                     RoleRepository roleRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.commentRepository = commentRepository;
        this.cuisineRepository = cuisineRepository;
        this.properties = properties;
        this.recipeRepository = recipeRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void postConstruct() throws Exception {
        switch (properties.getDdlAuto()) {
            case "validate":
                validateUploadsDirectory();
                break;
            case "update":
                createUploadsDirectory();
                break;
            case "create":
            case "create-drop":
                dropUploadsDirectory();
                createUploadsDirectory();
//                initDatabase();
                break;
        }
    }

    @PreDestroy
    public void preDestroy() {
        switch (properties.getDdlAuto()) {
            case "create-drop":
                dropUploadsDirectory();
                break;
        }
    }

    private void validateUploadsDirectory() {
        File uploadsDirectory = new File(properties.getUploadsPath());
        if (!uploadsDirectory.exists()) {
            log.error("Uploads directory does not exists");
        }
    }

    private void createUploadsDirectory() throws IOException {
        File uploadsDirectory = new File(properties.getUploadsPath());
        if (!uploadsDirectory.exists()) {
            log.info("Creating uploads directory");
            if (!uploadsDirectory.mkdirs()) {
                throw new IOException("Failed to create uploads directory");
            }
        }
    }

    private void dropUploadsDirectory() {
        File uploadsDirectory = new File(properties.getUploadsPath());
        if (uploadsDirectory.exists()) {
            log.info("Dropping uploads directory");
            FileSystemUtils.deleteRecursively(uploadsDirectory);
        }
    }

    private void initDatabase() {
        log.info("Initializing database");
        Role role1 = createRole("ROLE_ADMIN");

        User user1 = createUser("fitnut", "fitnut@example.com", null, null);
        User user2 = createUser("lalala", "lalala@example.com", null, null);
        User user3 = createUser("koko", "koko@example.com", null, Arrays.asList(user1, user2));
        User user4 = createUser("perlage", "perlage@example.com", null, null);
        User user5 = createUser("peper11111", "peper11111@example.com", Arrays.asList(role1), Arrays.asList(user3, user4));
        User user6 = createUser("mike11111", "mike11111@example.com", null, null);

        Cuisine cuisine1 = createCuisine("CUISINE_AMERICAN");
        Cuisine cuisine2 = createCuisine("CUISINE_ASIAN");
        Cuisine cuisine3 = createCuisine("CUISINE_CZECH");
        Cuisine cuisine4 = createCuisine("CUISINE_FRENCH");
        Cuisine cuisine5 = createCuisine("CUISINE_GREEK");
        Cuisine cuisine6 = createCuisine("CUISINE_ITALIAN");
        Cuisine cuisine7 = createCuisine("CUISINE_POLISH");
        Cuisine cuisine8 = createCuisine("CUISINE_SPANISH");

        Category category1 = createCategory("CATEGORY_SOUP");
        Category category2 = createCategory("CATEGORY_DESSERT");
        Category category3 = createCategory("CATEGORY_DRINK");
        Category category4 = createCategory("CATEGORY_PIZZA");

        Recipe recipe1 = createRecipe(user5, "Przepis 1", cuisine1, category1, Arrays.asList(user1, user2, user3));
        Recipe recipe2 = createRecipe(user5, "Przepis 2", cuisine2, category2, Arrays.asList(user1, user3, user4));
        Recipe recipe3 = createRecipe(user1, "Przepis 3", cuisine1, category3, null);
        Recipe recipe4 = createRecipe(user2, "Przepis 4", cuisine2, category2, null);
        Recipe recipe5 = createRecipe(user2, "Przepis 5", cuisine2, category2, null);
        Recipe recipe6 = createRecipe(user4, "Przepis 6", cuisine5, category1, null);

        Comment comment1 = createComment(user1, "Komentarz 1", recipe1, null);
        Comment comment2 = createComment(user2, "Komentarz 2", recipe1, comment1);
        Comment comment3 = createComment(user3, "Komentarz 3", recipe1, comment1);
        Comment comment4 = createComment(user4, "Komentarz 4", recipe2, null);
        Comment comment5 = createComment(user5, "Komentarz 5", recipe2, comment4);
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

    private Category createCategory(String name) {
        Category category = new Category(name);
        categoryRepository.save(category);
        return category;
    }

    private Comment createComment(User author, String content, Recipe recipe, Comment parent) {
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setRecipe(recipe);
        comment.setContent(content);
        comment.setParent(parent);
        commentRepository.save(comment);
        return comment;
    }

    private Recipe createRecipe(User author, String title, Cuisine cuisine, Category category, Collection<User> likes) {
        Recipe recipe = new Recipe();
        recipe.setAuthor(author);
        recipe.setTitle(title);
        recipe.setCuisine(cuisine);
        recipe.setCategory(category);
        recipe.setLikes(likes);
        recipe.setDifficulty(3);
        recipe.setPlates(4);
        recipe.setPreparationTime(90);
        recipe.setIngredients(Arrays.asList("sok z gumijagód", "wyborowa żytnia", "3/4 butelki wody"));
        recipe.setSteps(Arrays.asList("W pierwszej kolejności przygotuj składniki,", "Następnie pokrój papryke.", "Smacznego :)"));
        recipeRepository.save(recipe);
        return recipe;
    }
}
