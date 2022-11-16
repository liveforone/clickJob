package clickJob.clickJob.resume.model;

import clickJob.clickJob.users.model.Users;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String introduction;  //소개 및 설명

    @Column(columnDefinition = "TEXT")
    private String skill;  //직무 스킬넣기

    @Column(columnDefinition = "TEXT")
    private String location;
    private String academic; //학력

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private Users users;

    @Builder
    public Resume(Long id, String introduction, String skill, String location, String academic, Users users) {
        this.id = id;
        this.introduction = introduction;
        this.skill = skill;
        this.location = location;
        this.academic = academic;
        this.users = users;
    }
}
