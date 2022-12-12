package clickJob.clickJob.apply.service;

import clickJob.clickJob.apply.dto.ApplyJobResponse;
import clickJob.clickJob.apply.dto.ApplyRequest;
import clickJob.clickJob.apply.dto.ApplyUserResponse;
import clickJob.clickJob.apply.model.Apply;
import clickJob.clickJob.apply.repository.ApplyRepository;
import clickJob.clickJob.apply.util.ApplyMapper;
import clickJob.clickJob.job.model.Job;
import clickJob.clickJob.job.repository.JobRepository;
import clickJob.clickJob.users.model.Users;
import clickJob.clickJob.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplyService {

    private final ApplyRepository applyRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public List<ApplyUserResponse> getApplyUserList(String email) {
        return ApplyMapper.entityToDtoListUserResponse(
                applyRepository.findApplyByUser(email)
        );
    }

    public List<ApplyJobResponse> getApplyJobList(Long jobId) {
        return ApplyMapper.entityToDtoJobResponse(
                applyRepository.findApplyByJobId(jobId)
        );
    }

    public Apply getApplyEntity(Long jobId, String email) {
        Job job = jobRepository.findOneById(jobId);
        Users users = userRepository.findByEmail(email);

        return applyRepository.findOneApply(job, users);
    }

    @Transactional
    public void applyJob(Long jobId, String email) {
        Job job = jobRepository.findOneById(jobId);
        Users users = userRepository.findByEmail(email);

        ApplyRequest dto = ApplyRequest.builder()
                .job(job)
                .users(users)
                .build();

        applyRepository.save(
                ApplyMapper.dtoToEntity(dto)
        );
    }
}
