package clickJob.clickJob.resume.util;

import clickJob.clickJob.resume.dto.ResumeRequest;
import clickJob.clickJob.resume.model.Resume;

public class ResumeMapper {

    //== dto -> entity ==//
    public static Resume dtoToEntity(ResumeRequest resume) {
        return Resume.builder()
                .id(resume.getId())
                .introduction(resume.getIntroduction())
                .skill(resume.getSkill())
                .location(resume.getLocation())
                .academic(resume.getAcademic())
                .users(resume.getUsers())
                .build();
    }
}
