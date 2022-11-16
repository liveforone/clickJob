package clickJob.clickJob.job.dto;

import clickJob.clickJob.job.model.Job;
import clickJob.clickJob.users.model.Users;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JobRequest {

    private Long id;
    private String title;
    private String content;
    private String position;
    private String company;
    private String duty;
    private Users users;
    private int volunteer;

    public Job toEntity() {
        return Job.builder()
                .id(id)
                .title(title)
                .content(content)
                .position(position)
                .company(company)
                .duty(duty)
                .users(users)
                .volunteer(volunteer)
                .build();
    }
}
