package clickJob.clickJob.bookmark.controller;

import clickJob.clickJob.bookmark.model.Bookmark;
import clickJob.clickJob.bookmark.service.BookmarkService;
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

import java.net.URI;
import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final JobService jobService;

    @GetMapping("/my-bookmark")
    public ResponseEntity<Map<String, Object>> myBookmark(Principal principal) {
        Map<String, Object> bookmarkList =
                bookmarkService.getBookmarkList(principal.getName());

        return ResponseEntity.ok(bookmarkList);
    }

    @PostMapping("/bookmark/post/{jobId}")
    public ResponseEntity<?> bookmarking(
            @PathVariable("jobId") Long jobId,
            Principal principal
    ) {
        Job job = jobService.getJobDetail(jobId);
        Bookmark bookmark = bookmarkService.getBookmark(principal.getName(), jobId);

        if (CommonUtils.isNull(job)) {
            return ResponseEntity
                    .ok("해당 채용 공고를 찾을 수 없어 북마킹이 불가능합니다.");
        }

        if (!CommonUtils.isNull(bookmark)) {  //중복될 때
            return ResponseEntity.ok("이미 북마크 하였습니다.");
        }

        String url = "/job/" + jobId;
        HttpHeaders httpHeaders = CommonUtils.makeHeader(url);

        bookmarkService.saveBookmark(
                principal.getName(),
                jobId
        );
        log.info("북마킹 성공!!");

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }

    @PostMapping("/bookmark/cancel/{jobId}")
    public ResponseEntity<?> bookmarkCancel(
            @PathVariable("jobId") Long jobId,
            Principal principal
    ) {
        Job job = jobService.getJobDetail(jobId);
        Bookmark bookmark = bookmarkService.getBookmark(principal.getName(), jobId);

        if (CommonUtils.isNull(job)) {
            return ResponseEntity.ok("해당 채용공고를 찾을 수 없어 북마크 취소가 불가능합니다.");
        }

        if (CommonUtils.isNull(bookmark)) {
            return ResponseEntity.ok("해당 북마크가 없어 북마크 취소가 불가능합니다.");
        }

        String url = "/job/" + jobId;
        HttpHeaders httpHeaders = CommonUtils.makeHeader(url);

        bookmarkService.cancelBookmark(bookmark.getId());
        log.info("북마크 삭제 성공!!");

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .headers(httpHeaders)
                .build();
    }
}
