package exercise.controller.users;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import exercise.model.Post;
import exercise.Data;

// BEGIN
@RestController
@RequestMapping("/api")
public class PostsController {
    private List<Post> posts = Data.getPosts();
    @GetMapping("/users/{id}/posts")
    public List<Post> index(@PathVariable Integer id) {
        return posts.stream()
                .filter(p -> p.getUserId().equals(id))
                .toList();
    }

    @GetMapping("/posts")
    public List<Post> index(@RequestParam(defaultValue = "10") Integer limit, @RequestParam(defaultValue = "1") Integer page) {
        return posts.stream().skip((long) limit * (page - 1)).limit(limit).toList();
    }

    @PostMapping("/users/{id}/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@PathVariable Integer id, @RequestBody Post post) {
        post.setUserId(id);
        posts.add(post);
        return post;
    }
}
// END
