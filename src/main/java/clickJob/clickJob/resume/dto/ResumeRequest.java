package clickJob.clickJob.resume.dto;

import clickJob.clickJob.resume.model.Resume;
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

    public Resume toEntity() {
        return Resume.builder()
                .id(id)
                .introduction(introduction)
                .skill(skill)
                .location(location)
                .academic(academic)
                .users(users)
                .build();
    }
}
