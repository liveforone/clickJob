package clickJob.clickJob.resume.repository;

import clickJob.clickJob.resume.dto.ResumeResponse;
import clickJob.clickJob.resume.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    @Query("select r from Resume r join fetch r.users u where u.email = :email")
    Resume findByUserEmail(@Param("email") String email);

    @Query("select new clickJob.clickJob.resume.dto.ResumeResponse" +
            "(r.introduction, r.skill, r.location, r.academic)" +
            " from Resume r join r.users u where u.email = :email")
    ResumeResponse findOneDtoByUserEmail(@Param("email") String email);
}
