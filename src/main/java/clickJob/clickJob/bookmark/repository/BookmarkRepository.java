package clickJob.clickJob.bookmark.repository;

import clickJob.clickJob.bookmark.model.Bookmark;
import clickJob.clickJob.job.model.Job;
import clickJob.clickJob.users.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("select b from Bookmark b join fetch b.job join fetch b.users u where u.email = :email")
    List<Bookmark> findByUserEmail(@Param("email") String email);

    @Query("select b from Bookmark b join fetch b.users join fetch b.job where b.users = :users and b.job = :job")
    Bookmark findUsersAndBoard(@Param("users") Users users, @Param("job") Job job);
}
