package ku.chirpchat.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PostResponse {
   private UUID id;
   private String name;
   private String address;
   private int rating;
   private List<CommentResponse> comments;
}
