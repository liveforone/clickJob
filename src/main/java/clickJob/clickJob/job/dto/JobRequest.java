package clickJob.clickJob.job.dto;

import clickJob.clickJob.users.model.Users;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JobRequest {

    private Long id;
    private String title;
    private String content;
    private String position;
    private String company;
    private String duty;
    private Users users;
    private int volunteer;
}
