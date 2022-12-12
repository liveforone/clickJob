package clickJob.clickJob.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponse {

    private Long id;
    private String title;
    private String content;
    private int view;
    private int good;
    private LocalDate createdDate;
}
