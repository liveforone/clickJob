package clickJob.clickJob.job.model;

import clickJob.clickJob.users.model.Users;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String position;  //직급
    private String company;
    private String duty;  //직무

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private Users users;  //채용 공고 작성자

    @Column(columnDefinition = "integer default 0")
    private int volunteer;  //지원자 수

    @CreatedDate
    @Column(updatable = false)
    private LocalDate createdDate;

    @Builder
    public Job(Long id, String title, String content, String position, String company, String duty, Users users, int volunteer) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.position = position;
        this.company = company;
        this.duty = duty;
        this.users = users;
        this.volunteer = volunteer;
    }
}
