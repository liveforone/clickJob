package clickJob.clickJob.board.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class BoardResponse {

    private Long id;
    private String title;
    private String content;
    private int view;
    private int good;
    private LocalDate createdDate;

    @Builder
    public BoardResponse(Long id, String title, String content, int view, int good, LocalDate createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.view = view;
        this.good = good;
        this.createdDate = createdDate;
    }
}
