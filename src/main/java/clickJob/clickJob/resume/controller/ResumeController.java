package clickJob.clickJob.resume.controller;

import clickJob.clickJob.resume.dto.ResumeRequest;
import clickJob.clickJob.resume.model.Resume;
import clickJob.clickJob.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ResumeController {

    private final ResumeService resumeService;

    @GetMapping("/my-resume")
    public ResponseEntity<?> myResume(Principal principal) {
        Resume resume = resumeService.getResume(principal.getName());

        if (resume == null) {  //null -> 이력서 등록 페이지로 리다이렉트
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(URI.create("/resume/post"));

            return ResponseEntity
                    .status(HttpStatus.MOVED_PERMANENTLY)
                    .headers(httpHeaders)
                    .build();
        }

        return ResponseEntity.ok(resumeService.entityToDtoDetail(resume));
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
        resumeService.saveResume(resumeRequest, principal.getName());
        log.info("이력서 작성 성공");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/my-resume"));

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }

    @GetMapping("/resume/edit")
    public ResponseEntity<?> resumeEditPage(Principal principal) {
        Resume resume = resumeService.getResume(principal.getName());

        if (resume == null) {
            return ResponseEntity.ok("이력서가 없어 수정이 불가능합니다.");
        }

        return ResponseEntity.ok(resumeService.entityToDtoDetail(resume));
    }

    @PostMapping("/resume/edit")
    public ResponseEntity<?> resumeEdit(
            @RequestBody ResumeRequest resumeRequest,
            Principal principal
    ) {
        resumeService.editResume(resumeRequest, principal.getName());
        log.info("이력서 수정 성공");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/my-resume"));

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }
}
