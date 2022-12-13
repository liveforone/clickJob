package clickJob.clickJob.apply.controller;

import clickJob.clickJob.apply.dto.ApplyJobResponse;
import clickJob.clickJob.apply.dto.ApplyUserResponse;
import clickJob.clickJob.apply.model.Apply;
import clickJob.clickJob.apply.service.ApplyService;
import clickJob.clickJob.job.model.Job;
import clickJob.clickJob.job.service.JobService;
import clickJob.clickJob.utility.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ApplyController {

    private final ApplyService applyService;
    private final JobService jobService;

    @GetMapping("/apply-list/{jobId}")
    public ResponseEntity<?> applyList(
            @PathVariable("jobId") Long jobId,
            Principal principal
    ) {
        Job job = jobService.getJobDetail(jobId);

        if (CommonUtils.isNull(job)) {
            return ResponseEntity.ok("해당 채용 공고가 존재하지 않습니다.");
        }

        if (!Objects.equals(job.getUsers().getEmail(), principal.getName())) {
            return ResponseEntity
                    .ok("작성자와 회원님이 달라 구직신청 리스트를 볼 수 없습니다.");
        }

        List<ApplyJobResponse> applyList = applyService.getApplyJobList(jobId);

        return ResponseEntity.ok(applyList);
    }

    @GetMapping("/my-apply")
    public ResponseEntity<?> getApplyListForMyPage(Principal principal) {
        List<ApplyUserResponse> applyList =
                applyService.getApplyUserList(principal.getName());

        if (CommonUtils.isNull(applyList)) {
            return ResponseEntity.ok("지원한것이 없습니다.");
        }

        return ResponseEntity.ok(applyList);
    }

    @PostMapping("/apply/{jobId}")
    public ResponseEntity<?> applyJob(
            @PathVariable("jobId") Long jobId,
            Principal principal
    ) {
        Job job = jobService.getJobDetail(jobId);
        Apply apply = applyService.getApplyEntity(jobId, principal.getName());

        if (CommonUtils.isNull(job)) {
            return ResponseEntity.ok("해당 채용공고가 없어 구직신청이 불가능합니다.");
        }

        if (!CommonUtils.isNull(apply)) {  //중복 될 때
            return ResponseEntity.ok("이미 구직신청 되었습니다.");
        }

        String url = "/job/" + jobId;
        HttpHeaders httpHeaders = CommonUtils.makeHeader(url);

        applyService.applyJob(
                jobId,
                principal.getName()
        );
        jobService.updateVolunteer(jobId);
        log.info("구직 신청 성공!!");

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }
}
