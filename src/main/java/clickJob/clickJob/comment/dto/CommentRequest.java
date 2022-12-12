package clickJob.clickJob.comment.dto;

import clickJob.clickJob.board.model.Board;
import clickJob.clickJob.users.model.Users;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentRequest {

    private Long id;
    private Users users;
    private String content;
    private Board board;
    private int good;
}
