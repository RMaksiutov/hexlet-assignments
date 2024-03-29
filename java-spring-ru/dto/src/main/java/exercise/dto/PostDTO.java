package exercise.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

// BEGIN
@Setter
@Getter
public class PostDTO {
    private long id;
    private String title;
    private String body;
    private ArrayList<CommentDTO> comments;
}
// END
