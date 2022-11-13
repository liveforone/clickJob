package clickJob.clickJob.board.dto;

import clickJob.clickJob.board.model.Board;
import clickJob.clickJob.users.model.Users;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardRequest {

    private Long id;
    private String title;
    private String content;
    private Users users;
    private int view;
    private int good;

    public Board toEntity() {
        return Board.builder()
                .id(id)
                .title(title)
                .content(content)
                .users(users)
                .view(view)
                .good(good)
                .build();
    }
}
