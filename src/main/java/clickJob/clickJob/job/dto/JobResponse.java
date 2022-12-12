package clickJob.clickJob.job.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobResponse {

    private Long id;
    private String title;
    private String content;
    private String position;
    private String company;
    private String duty;
    private int volunteer;
    private LocalDate createdDate;
}
