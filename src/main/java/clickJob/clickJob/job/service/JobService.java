package clickJob.clickJob.job.service;

import clickJob.clickJob.job.dto.JobRequest;
import clickJob.clickJob.job.dto.JobResponse;
import clickJob.clickJob.job.model.Job;
import clickJob.clickJob.job.repository.JobRepository;
import clickJob.clickJob.users.model.Users;
import clickJob.clickJob.users.repository.UserRepository;
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

    //== entity ->  dto 편의메소드1 - 페이징 형식 ==//
    public Page<JobResponse> entityToDtoPage(Page<Job> jobList) {
        return jobList.map(m -> JobResponse.builder()
                .id(m.getId())
                .title(m.getTitle())
                .content(m.getContent())
                .position(m.getPosition())
                .company(m.getCompany())
                .duty(m.getDuty())
                .volunteer(m.getVolunteer())
                .createdDate(m.getCreatedDate())
                .build()
        );
    }

    //== entity -> dto 편의메소드2 - 엔티티 하나 ==//
    public JobResponse entityToDtoDetail(Job job) {
        if (job != null) {
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
        } else {
            return null;
        }
    }

    public Page<JobResponse> getAllJob(Pageable pageable) {
        return entityToDtoPage(jobRepository.findAll(pageable));
    }

    public Page<JobResponse> getJobSearchList(String keyword, Pageable pageable) {
        return entityToDtoPage(jobRepository.findSearchByTitle(keyword, pageable));
    }

    public Job getJobDetail(Long id) {
        return jobRepository.findOneById(id);
    }

    @Transactional
    public Long saveJob(JobRequest jobRequest, String email) {
        Users users = userRepository.findByEmail(email);

        jobRequest.setUsers(users);

        return jobRepository.save(jobRequest.toEntity()).getId();
    }

    @Transactional
    public void editJob(JobRequest jobRequest, Long id) {
        Job job = jobRepository.findOneById(id);

        jobRequest.setId(id);
        jobRequest.setUsers(job.getUsers());
        jobRequest.setVolunteer(job.getVolunteer());

        jobRepository.save(jobRequest.toEntity());
    }

    @Transactional
    public void updateVolunteer(Long id) {
        jobRepository.updateVolunteer(id);
    }
}
