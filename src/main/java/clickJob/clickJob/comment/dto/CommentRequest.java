package clickJob.clickJob.comment.dto;

import clickJob.clickJob.board.model.Board;
import clickJob.clickJob.users.model.Users;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentRequest {

    private Long id;
    private Users users;
    private String content;
    private Board board;
    private int good;
}
