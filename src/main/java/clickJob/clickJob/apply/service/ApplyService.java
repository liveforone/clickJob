package clickJob.clickJob.apply.service;

import clickJob.clickJob.apply.dto.ApplyJobResponse;
import clickJob.clickJob.apply.dto.ApplyRequest;
import clickJob.clickJob.apply.dto.ApplyUserResponse;
import clickJob.clickJob.apply.model.Apply;
import clickJob.clickJob.apply.repository.ApplyRepository;
import clickJob.clickJob.job.model.Job;
import clickJob.clickJob.job.repository.JobRepository;
import clickJob.clickJob.users.model.Users;
import clickJob.clickJob.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplyService {

    private final ApplyRepository applyRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    //entity -> dto1 - user response list ==//
    public List<ApplyUserResponse> entityToDtoListUserResponse(List<Apply> applyList) {
        List<ApplyUserResponse> list = new ArrayList<>();

        for (Apply apply : applyList) {
            ApplyUserResponse dto = ApplyUserResponse.builder()
                    .company(apply.getJob().getCompany())
                    .build();
            list.add(dto);
        }

        return list;
    }

    //== entity -> dto2 - job response list ==//
    public List<ApplyJobResponse> entityToDtoJobResponse(List<Apply> applyList) {
        List<ApplyJobResponse> list = new ArrayList<>();

        for (Apply apply : applyList) {
            ApplyJobResponse dto = ApplyJobResponse.builder()
                    .name(apply.getUsers().getNickname())
                    .build();
            list.add(dto);
        }

        return list;
    }

    public List<ApplyUserResponse> getApplyUserList(String email) {
        return entityToDtoListUserResponse(applyRepository.findApplyByUser(email));
    }

    public List<ApplyJobResponse> getApplyJobList(Long jobId) {
        return entityToDtoJobResponse(applyRepository.findApplyByJobId(jobId));
    }

    public Apply getApplyDetail(Long jobId, String email) {
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

        applyRepository.save(dto.toEntity());
    }
}
