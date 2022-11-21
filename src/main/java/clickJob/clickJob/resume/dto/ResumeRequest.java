package clickJob.clickJob.resume.dto;

import clickJob.clickJob.users.model.Users;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResumeRequest {

    private Long id;
    private String introduction;
    private String skill;
    private String location;
    private String academic;
    private Users users;
}
