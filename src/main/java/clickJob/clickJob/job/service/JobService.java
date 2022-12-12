package clickJob.clickJob.job.service;

import clickJob.clickJob.job.dto.JobRequest;
import clickJob.clickJob.job.dto.JobResponse;
import clickJob.clickJob.job.model.Job;
import clickJob.clickJob.job.repository.JobRepository;
import clickJob.clickJob.job.util.JobMapper;
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

    public Page<JobResponse> getAllJob(Pageable pageable) {
        return JobMapper.entityToDtoPage(
                jobRepository.findAll(pageable)
        );
    }

    public Page<JobResponse> getJobSearchList(String keyword, Pageable pageable) {
        return JobMapper.entityToDtoPage(
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
                JobMapper.dtoToEntity(jobRequest)).getId();
    }

    @Transactional
    public void editJob(JobRequest jobRequest, Long id) {
        Job job = jobRepository.findOneById(id);

        jobRequest.setId(id);
        jobRequest.setUsers(job.getUsers());
        jobRequest.setVolunteer(job.getVolunteer());

        jobRepository.save(
                JobMapper.dtoToEntity(jobRequest)
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
