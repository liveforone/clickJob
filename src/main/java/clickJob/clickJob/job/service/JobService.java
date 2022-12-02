package clickJob.clickJob.job.service;

import clickJob.clickJob.job.dto.JobRequest;
import clickJob.clickJob.job.dto.JobResponse;
import clickJob.clickJob.job.model.Job;
import clickJob.clickJob.job.repository.JobRepository;
import clickJob.clickJob.users.model.Users;
import clickJob.clickJob.users.repository.UserRepository;
import clickJob.clickJob.utility.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    //== JobResponse builder method ==//
    public JobResponse dtoBuilder(Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .content(job.getContent())
                .position(job.getPosition())
                .company(job.getCompany())
                .duty(job.getDuty())
                .volunteer(job.getVolunteer())
                .createdDate(job.getCreatedDate())
                .build();
    }

    //== dto -> entity ==//
    public Job dtoToEntity(JobRequest job) {
        return Job.builder()
                .id(job.getId())
                .title(job.getTitle())
                .content(job.getContent())
                .position(job.getPosition())
                .company(job.getCompany())
                .duty(job.getDuty())
                .users(job.getUsers())
                .volunteer(job.getVolunteer())
                .build();
    }

    //== entity ->  dto 편의메소드1 - 페이징 형식 ==//
    public Page<JobResponse> entityToDtoPage(Page<Job> jobList) {
        return jobList.map(this::dtoBuilder);
    }

    //== entity -> dto 편의메소드2 - 엔티티 하나 ==//
    public JobResponse entityToDtoDetail(Job job) {
        if (CommonUtils.isNull(job)) {
            return null;
        }
        return dtoBuilder(job);
    }

    public Page<JobResponse> getAllJob(Pageable pageable) {
        return entityToDtoPage(
                jobRepository.findAll(pageable)
        );
    }

    public Page<JobResponse> getJobSearchList(String keyword, Pageable pageable) {
        return entityToDtoPage(
                jobRepository.searchByTitle(
                        keyword,
                        pageable
                )
        );
    }

    public Job getJobDetail(Long id) {
        return jobRepository.findOneById(id);
    }

    @Transactional
    public Long saveJob(JobRequest jobRequest, String email) {
        Users users = userRepository.findByEmail(email);

        jobRequest.setUsers(users);

        return jobRepository.save(
                dtoToEntity(jobRequest)).getId();
    }

    @Transactional
    public void editJob(JobRequest jobRequest, Long id) {
        Job job = jobRepository.findOneById(id);

        jobRequest.setId(id);
        jobRequest.setUsers(job.getUsers());
        jobRequest.setVolunteer(job.getVolunteer());

        jobRepository.save(
                dtoToEntity(jobRequest)
        );
    }

    @Transactional
    public void updateVolunteer(Long id) {
        jobRepository.updateVolunteer(id);
    }

    @Transactional
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }
}
