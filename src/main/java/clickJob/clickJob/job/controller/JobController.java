package clickJob.clickJob.job.controller;

import clickJob.clickJob.job.dto.JobRequest;
import clickJob.clickJob.job.dto.JobResponse;
import clickJob.clickJob.job.model.Job;
import clickJob.clickJob.job.service.JobService;
import clickJob.clickJob.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class JobController {

    private final JobService jobService;
    private final UserService userService;

    @GetMapping("/job")
    public ResponseEntity<Page<JobResponse>> jobHome(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<JobResponse> job = jobService.getAllJob(pageable);

        return ResponseEntity.ok(job);
    }

    @GetMapping("/job/search")
    public ResponseEntity<Page<JobResponse>> searchJob(
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "volunteer", direction = Sort.Direction.DESC),
                    @SortDefault(sort = "id", direction = Sort.Direction.DESC)
            }) Pageable pageable,
            @RequestParam("keyword") String keyword
    ) {
        Page<JobResponse> job = jobService.getJobSearchList(keyword, pageable);

        return ResponseEntity.ok(job);
    }

    @GetMapping("/job/post")
    public ResponseEntity<?> jobPostPage() {
        return ResponseEntity.ok("채용 공고 작성 페이지");
    }

    @PostMapping("/job/post")
    public ResponseEntity<?> jobPost(
            @RequestBody JobRequest jobRequest,
            Principal principal
    ) {
        Long jobId = jobService.saveJob(jobRequest, principal.getName());
        log.info("채용 공고 작성 성공");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/job/" + jobId));

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }

    /*
    여기에서 apply, 즉 구직버튼을 클릭함.
     */
    @GetMapping("/job/{id}")
    public ResponseEntity<?> jobDetail(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        Job job = jobService.getJobDetail(id);

        if (job == null) {
            return ResponseEntity.ok("마감되었거나 존재하지 않는 채용공고 입니다.");
        }

        Map<String, Object> map = new HashMap<>();
        String writer = job.getUsers().getNickname();
        String user = userService.getUserByEmail(principal.getName()).getNickname();

        map.put("job", jobService.entityToDtoDetail(job));
        map.put("writer", writer);
        map.put("user", user);

        return ResponseEntity.ok(map);
    }

    @GetMapping("/job/edit/{id}")
    public ResponseEntity<?> editPage(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        Job job = jobService.getJobDetail(id);

        if (job == null) {
            return ResponseEntity.ok("채용공고가 존재하지않아 수정이 불가능합니다.");
        }

        if (!Objects.equals(job.getUsers().getEmail(), principal.getName())) {
            return ResponseEntity.ok("작성자와 회원님이 달라 수정이 불가능합니다.");
        }

        return ResponseEntity.ok(jobService.entityToDtoDetail(job));
    }

    @PostMapping("/job/edit/{id}")
    public ResponseEntity<?> editJob(
            @PathVariable("id") Long id,
            @RequestBody JobRequest jobRequest,
            Principal principal
    ) {
        Job job = jobService.getJobDetail(id);

        if (job == null) {
            return ResponseEntity.ok("채용공고가 존재하지않아 수정이 불가능합니다.");
        }

        if (!Objects.equals(job.getUsers().getEmail(), principal.getName())) {
            return ResponseEntity.ok("작성자와 회원님이 달라 수정이 불가능합니다.");
        }

        jobService.editJob(jobRequest, id);
        log.info("채용 공고 수정 완료");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/job/" + id));

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }

    @PostMapping("/job/delete/{id}")
    public ResponseEntity<?> jobDelete(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        Job job = jobService.getJobDetail(id);

        if (job == null) {
            return ResponseEntity.ok("마감되었거나 존재하지 않는 채용공고 입니다.");
        }

        if (!Objects.equals(job.getUsers().getEmail(), principal.getName())) {
            return ResponseEntity.ok("작성자와 회원님이 달라 채용공고 마감이 불가능합니다.");
        }

        jobService.deleteJob(id);
        log.info("채용공고 id=" + id + " 가 마감되었습니다.");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/job"));

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }
}
