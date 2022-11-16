package clickJob.clickJob.resume.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResumeResponse {

    private String introduction;
    private String skill;
    private String location;
    private String academic;

    @Builder
    public ResumeResponse(String introduction, String skill, String location, String academic) {
        this.introduction = introduction;
        this.skill = skill;
        this.location = location;
        this.academic = academic;
    }
}
