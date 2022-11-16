package clickJob.clickJob.resume.repository;

import clickJob.clickJob.resume.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    @Query("select r from Resume r join fetch r.users u where u.email = :email")
    Resume findByUserEmail(@Param("email") String email);
}
