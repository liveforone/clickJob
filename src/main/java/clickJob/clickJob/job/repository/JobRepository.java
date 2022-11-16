package clickJob.clickJob.job.repository;

import clickJob.clickJob.job.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("select j from Job j join fetch j.users where j.id = :id")
    Job findOneById(@Param("id") Long id);

    @Query("select j from Job j join j.users where j.title like %:title%")
    Page<Job> findSearchByTitle(@Param("title") String keyword, Pageable pageable);

    @Modifying
    @Query("update Job j set j.volunteer = j.volunteer + 1 where j.id = :id")
    void updateVolunteer(@Param("id") Long id);
}
