package clickJob.clickJob.resume.dto;

import clickJob.clickJob.users.model.Users;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResumeRequest {

    private Long id;
    private String introduction;
    private String skill;
    private String location;
    private String academic;
    private Users users;
}
