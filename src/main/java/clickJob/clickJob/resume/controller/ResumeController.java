package clickJob.clickJob.resume.controller;

import clickJob.clickJob.resume.dto.ResumeRequest;
import clickJob.clickJob.resume.dto.ResumeResponse;
import clickJob.clickJob.resume.service.ResumeService;
import clickJob.clickJob.utility.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ResumeController {

    private final ResumeService resumeService;

    @GetMapping("/my-resume")
    public ResponseEntity<?> myResume(Principal principal) {
        ResumeResponse resume = resumeService.getResumeResponse(principal.getName());

        if (CommonUtils.isNull(resume)) {  //null -> 이력서 등록 페이지로 리다이렉트
            String url = "/resume/post";
            HttpHeaders httpHeaders = CommonUtils.makeHeader(url);

            return ResponseEntity
                    .status(HttpStatus.MOVED_PERMANENTLY)
                    .headers(httpHeaders)
                    .build();
        }

        return ResponseEntity.ok(resume);
    }

    @GetMapping("/resume/post")
    public ResponseEntity<?> resumePostPage() {
        return ResponseEntity.ok("이력서 작성 페이지 입니다.");
    }

    @PostMapping("/resume/post")
    public ResponseEntity<?> resumePost(
            @RequestBody ResumeRequest resumeRequest,
            Principal principal
    ) {
        resumeService.saveResume(
                resumeRequest,
                principal.getName()
        );
        log.info("이력서 작성 성공");

        String url = "/my-resume";
        HttpHeaders httpHeaders = CommonUtils.makeHeader(url);

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }

    @GetMapping("/resume/edit")
    public ResponseEntity<?> resumeEditPage(Principal principal) {
        ResumeResponse resume = resumeService.getResumeResponse(principal.getName());

        if (CommonUtils.isNull(resume)) {
            return ResponseEntity.ok("이력서가 없어 수정이 불가능합니다.");
        }

        return ResponseEntity.ok(resume);
    }

    @PostMapping("/resume/edit")
    public ResponseEntity<?> resumeEdit(
            @RequestBody ResumeRequest resumeRequest,
            Principal principal
    ) {
        resumeService.editResume(
                resumeRequest,
                principal.getName()
        );
        log.info("이력서 수정 성공");

        String url = "/my-resume";
        HttpHeaders httpHeaders = CommonUtils.makeHeader(url);

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }
}
