package clickJob.clickJob.board.dto;

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
}
