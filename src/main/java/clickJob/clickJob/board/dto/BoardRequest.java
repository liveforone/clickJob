package clickJob.clickJob.board.dto;

import clickJob.clickJob.users.model.Users;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardRequest {

    private Long id;
    private String title;
    private String content;
    private Users users;
    private int view;
    private int good;
}
