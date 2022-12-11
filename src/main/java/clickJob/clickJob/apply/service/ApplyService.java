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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplyService {

    private final ApplyRepository applyRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    //== dto -> entity ==//
    public Apply dtoToEntity(ApplyRequest apply) {
        return Apply.builder()
                .id(apply.getId())
                .users(apply.getUsers())
                .job(apply.getJob())
                .build();
    }

    //== ApplyUserResponse builder ==//
    public ApplyUserResponse userDtoBuilder(Apply apply) {
        return ApplyUserResponse.builder()
                .company(apply.getJob().getCompany())
                .build();
    }

    //== ApplyJobResponse builder ==//
    public ApplyJobResponse jobDtoBuilder(Apply apply) {
        return ApplyJobResponse.builder()
                .name(apply.getUsers().getNickname())
                .build();
    }

    //entity -> dto1 - user response list ==//
    public List<ApplyUserResponse> entityToDtoListUserResponse(List<Apply> applyList) {
        return applyList
                .stream()
                .map(this::userDtoBuilder)
                .collect(Collectors.toList());
    }

    //== entity -> dto2 - job response list ==//
    public List<ApplyJobResponse> entityToDtoJobResponse(List<Apply> applyList) {
        return applyList
                .stream()
                .map(this::jobDtoBuilder)
                .collect(Collectors.toList());
    }

    public List<ApplyUserResponse> getApplyUserList(String email) {
        return entityToDtoListUserResponse(
                applyRepository.findApplyByUser(email)
        );
    }

    public List<ApplyJobResponse> getApplyJobList(Long jobId) {
        return entityToDtoJobResponse(
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
                dtoToEntity(dto)
        );
    }
}
