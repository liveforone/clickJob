package clickJob.clickJob.job.util;

import clickJob.clickJob.job.dto.JobRequest;
import clickJob.clickJob.job.dto.JobResponse;
import clickJob.clickJob.job.model.Job;
import clickJob.clickJob.utility.CommonUtils;
import org.springframework.data.domain.Page;

public class JobMapper {

    /*
     * dto ->  entity 변환 편의 메소드
     */
    public static Job dtoToEntity(JobRequest job) {
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

    /*
     * JobResponse builder 편의 메소드
     */
    private static JobResponse dtoBuilder(Job job) {
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

    /*
     * entity ->  dto 편의 메소드1
     * 반환 타입 : 페이징 형식
     */
    public static Page<JobResponse> entityToDtoPage(Page<Job> jobList) {
        return jobList.map(JobMapper::dtoBuilder);
    }

    /*
     * entity -> dto 편의 메소드2
     * 반환 타입 : 엔티티 하나
     */
    public static JobResponse entityToDtoDetail(Job job) {
        if (CommonUtils.isNull(job)) {
            return null;
        }
        return JobMapper.dtoBuilder(job);
    }
}
