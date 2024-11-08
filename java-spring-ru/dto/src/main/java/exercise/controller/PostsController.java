package exercise.controller;

import exercise.model.Comment;
import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;
import exercise.dto.PostDTO;
import exercise.dto.CommentDTO;

// BEGIN
@RestController
@RequestMapping("/posts")
public class PostsController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping()
    public List<PostDTO> getPost() {
        List<Post> postList = postRepository.findAll();

        return postList.stream()
                .map(this::toPostDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public PostDTO getPost(@PathVariable Long id) {
        var result = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
        var comments = commentRepository.findByPostId(id);

        var post = toPostDTO(result);

        post.setComments(comments.stream()
                .map(this::toCommentDTO)
                .toList());

        return post;
    }

    private PostDTO toPostDTO(Post post) {
        var dto = new PostDTO();

        dto.setTitle(post.getTitle());
        dto.setBody(post.getBody());
        dto.setId(post.getId());

        return dto;
    }

    private CommentDTO toCommentDTO(Comment comment) {
        var dto = new CommentDTO();

        dto.setId(comment.getId());
        dto.setBody(comment.getBody());

        return dto;
    }
}
// END
