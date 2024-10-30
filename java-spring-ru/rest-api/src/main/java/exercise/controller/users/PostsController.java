package exercise.controller.users;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import exercise.model.Post;
import exercise.Data;

// BEGIN
@RestController
@RequestMapping("/api")
public class PostsController {

    private List<Post> posts = Data.getPosts();

    @GetMapping("/users/{id}/posts")
    public ResponseEntity<List<Post>> getUsers(@PathVariable String id) {
        var payload = posts.stream().filter(p -> p.getUserId() == Integer.parseInt(id)).toList();

        return ResponseEntity.status(HttpStatus.OK).body(payload);
    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> createPost(@PathVariable String id, @RequestBody Post post) {
        var payload = new Post();
        payload.setUserId(Integer.parseInt(id));
        payload.setBody(post.getBody());
        payload.setTitle(post.getTitle());
        payload.setSlug(post.getSlug());

        posts.add(payload);

        return ResponseEntity.status(HttpStatus.CREATED).body(payload);
    }
}
// END
