package clickJob.clickJob.apply.dto;

import clickJob.clickJob.apply.model.Apply;
import clickJob.clickJob.job.model.Job;
import clickJob.clickJob.users.model.Users;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApplyRequest {

    private Long id;
    private Users users;
    private Job job;

    @Builder
    public ApplyRequest(Long id, Users users, Job job) {
        this.id = id;
        this.users = users;
        this.job = job;
    }
}
