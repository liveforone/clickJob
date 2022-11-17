package clickJob.clickJob.bookmark.service;

import clickJob.clickJob.bookmark.model.Bookmark;
import clickJob.clickJob.bookmark.repository.BookmarkRepository;
import clickJob.clickJob.job.model.Job;
import clickJob.clickJob.job.repository.JobRepository;
import clickJob.clickJob.users.model.Users;
import clickJob.clickJob.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    //== entity -> map id & title ==//
    public Map<String, Object> entityToMap(List<Bookmark> bookmarkList) {
        Map<String, Object> map = new HashMap<>();
        List<Long> jobId = new ArrayList<>();
        List<String> jobTitle = new ArrayList<>();

        for (Bookmark bookmark : bookmarkList) {
            jobId.add(bookmark.getJob().getId());
            jobTitle.add(bookmark.getJob().getTitle());
        }

        map.put("jobId", jobId);
        map.put("jobTitle", jobTitle);

        return map;
    }

    public Map<String, Object> getBookmarkList(String email) {
        return entityToMap(bookmarkRepository.findByUserEmail(email));
    }

    public Bookmark getBookmark(String email, Long jobId) {
        Users users = userRepository.findByEmail(email);
        Job job = jobRepository.findOneById(jobId);

        return bookmarkRepository.findOneBookmark(users, job);
    }

    @Transactional
    public void saveBookmark(String email, Long jobId) {
        Users users = userRepository.findByEmail(email);
        Job job = jobRepository.findOneById(jobId);

        Bookmark bookmark = Bookmark.builder()
                .users(users)
                .job(job)
                .build();

        bookmarkRepository.save(bookmark);
    }

    @Transactional
    public void bookmarkCancel(Long id) {
        bookmarkRepository.deleteById(id);
    }
}
