package clickJob.clickJob.comment.dto;

import clickJob.clickJob.board.model.Board;
import clickJob.clickJob.users.model.Users;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentResponse {

    private Long id;
    private String content;
    private String writer;
    private int good;
    private LocalDateTime createdDate;

    @Builder
    public CommentResponse(Long id, String content, String writer, int good, LocalDateTime createdDate) {
        this.id = id;
        this.content = content;
        this.writer = writer;
        this.good = good;
        this.createdDate = createdDate;
    }
}
