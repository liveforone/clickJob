package clickJob.clickJob.apply.repository;

import clickJob.clickJob.apply.model.Apply;
import clickJob.clickJob.job.model.Job;
import clickJob.clickJob.users.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplyRepository extends JpaRepository<Apply, Long> {

    @Query("select a from Apply a join fetch a.users join fetch a.job j where j.id = :id")
    List<Apply> findApplyByJobId(@Param("id") Long id);

    @Query("select a from Apply a join fetch a.job join fetch a.users u where u.email = :email")
    List<Apply> findApplyByUser(@Param("email") String email);

    @Query("select a from Apply a join fetch a.job join fetch a.users where a.job = :job and a.users = :users")
    Apply findOneApply(@Param("job") Job job, @Param("users") Users users);
}
