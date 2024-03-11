package exercise.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class Post {
    private Integer userId;
    private String slug;
    private String title;
    private String body;
}
