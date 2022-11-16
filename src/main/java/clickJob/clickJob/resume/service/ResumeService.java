package clickJob.clickJob.resume.service;

import clickJob.clickJob.resume.dto.ResumeRequest;
import clickJob.clickJob.resume.dto.ResumeResponse;
import clickJob.clickJob.resume.model.Resume;
import clickJob.clickJob.resume.repository.ResumeRepository;
import clickJob.clickJob.users.model.Users;
import clickJob.clickJob.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    //== entity -> dto 편의메소드 - 엔티티 하나 ==//
    public ResumeResponse entityToDtoDetail(Resume resume) {

        if (resume != null) {
            return ResumeResponse.builder()
                    .introduction(resume.getIntroduction())
                    .skill(resume.getSkill())
                    .location(resume.getLocation())
                    .academic(resume.getAcademic())
                    .build();
        } else {
            return null;
        }
    }

    public Resume getResume(String email) {
        return resumeRepository.findByUserEmail(email);
    }

    @Transactional
    public void saveResume(ResumeRequest resumeRequest, String email) {
        Users users = userRepository.findByEmail(email);

        resumeRequest.setUsers(users);

        resumeRepository.save(resumeRequest.toEntity());
    }

    @Transactional
    public void editResume(ResumeRequest resumeRequest, String email) {
        Resume resume = resumeRepository.findByUserEmail(email);

        resumeRequest.setId(resume.getId());
        resumeRequest.setUsers(resume.getUsers());

        resumeRepository.save(resumeRequest.toEntity());
    }
}
