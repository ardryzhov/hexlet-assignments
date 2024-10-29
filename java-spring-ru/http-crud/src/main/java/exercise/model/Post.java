package exercise.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Post {
    private String id;
    private String title;
    private String body;
}
