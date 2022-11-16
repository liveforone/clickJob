package clickJob.clickJob.job.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class JobResponse {

    private Long id;
    private String title;
    private String content;
    private String position;
    private String company;
    private String duty;
    private int volunteer;
    private LocalDate createdDate;

    @Builder
    public JobResponse(Long id, String title, String content, String position, String company, String duty, int volunteer, LocalDate createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.position = position;
        this.company = company;
        this.duty = duty;
        this.volunteer = volunteer;
        this.createdDate = createdDate;
    }
}
