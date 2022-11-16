# clickJob
> 구인 구직 플랫폼. 정규직부터 비정규직까지. 상시채용 전문 플랫폼.

## 기술 스택
* Spring Boot 2.7.5
* Language : Java17
* DB : MySql
* ORM : Spring Data Jpa
* Spring Security
* Spring validation
* LomBok
* Gradle
* Apache commons lang3

# 1. 설명
* 일을 잡는 구직 플랫폼이다. 링크드인에서 많이 영감을 얻었다.
* 구인 구직도 하면서 본인의 생각을 작성할 수 있는 게시판도 있다.
* 단순히 구직만 하는 플랫폼을 넘어 소통의 기능까지 겸비한 플랫폼이다.
* rest-api server
* 일정이 정해져 있는 정기 공채보다는 상시채용 위주의 구직 플랫폼이다.
* 정기 공채의 경우 제목란에 채용 일정을 같이 기입하여 사용한다.

# 2. 설계
* 회원은 닉네임이 존재해야하며, 닉네임 변경, id & pw 변경이 가능하여야한다.
* 여타 구직 사이트를 참고하면 사진이 없는 것을 알 수 있다. 파일 업로드는 하지 않는다.
* 피드에 내가 팔로우한 사람들이 올린 글만 올라와야한다.
* 올라온 구인 구직글은 북마크가 가능하여야한다.
* 구인 구직글에 현재까지 지원한 사람 수가 적혀 있어야한다.
* 마감날짜는 플랫폼에서 관리 하지 않는다. 일반적으로 상시 채용글을 플랫폼에 올리는 것을 권장하며,ㅡ
* 상시 채용글이 아닐경우 구직글에 제목 또는 설명란에 마감 날짜를 작성자가 직접 기입한다.
* 구직 신청리스트는 오직 작성자만 열람 가능하다.
* 쇼핑몰처럼 seller와 같은 권한 부여는 딱히 하지 않는다.
* 뷰에 보여주는 모든 유저이름은 이메일이 아닌 닉네임이다. 이메일은 오직 작성자 검증에서만 사용한다.

## ERD Diagram

## API 설계
### users
```
/ - get
/user/signup - get/post
/user/login - get/post
/user/prohibition - get
/user/mypage - get
/user/profile/{nickname} - get
/user/nickname-post - post
/user/search - get, param : string nickname
/admin - get, auth : admin
/user/change-email - post
/user/change-password - post
/user/withdraw - post, body : string password
```
### board
```
/board - get
/board/best - get
/board/search - get, param : string keyword
/board/post - get/post
/board/{id} - get
/board/image/{saveFileName} - get, file
/board/good/{id} - post
/board/edit/{id} - get/post
/board/delete/{id} - post
```
### comment
```
/comment/{boardId} - get 
/comment/post/{boardId} - post
/comment/edit/{id} - get/post
/comment/good/{id} - post
/comment/delete/{id} - post
```
### follow
```
/follow/{nickname} - post
/unfollow/{nickname} - post
/follow/my-follow - get
/follow/my-follower - get
/follow/profile-follow/{nickname} - get
/follow/profile-follower/{nickname} - get
```
### resume
```
/my-resume - get
/resume/post - get/post
/resume/edit - get/post
```
### job
```
/job - get
/job/search - get
/job/post - get/post
/job/{id} - get
/job/edit/{id} - get/post
/job/delete/{id} - post
```

## Json Body
### users
```
{
    "email" : "yc1234@gmail.com",
    "password" : "1234"
}
{
    "email" : "admin@breve.com",
    "password" : "1234"
}
{
    "email" : "ms1234@gmail.com",
    "password" : "1234"
}
{
    "oldPassword" : "1234",
    "newPassword" : "1111"
}
```
### board
```
{
    "title" : "test title",
    "content" : "this is test content"
}
```
### comment
```
{
    "content" : "this is comment"
}
this is updated comment - text
```
### resume
```
{
    "introduction" : "저는 개발자 chan kim 입니다.",
    "skill" : "Spring(spring boot), Jpa, Security, my-sql",
    "location" : "seoul",
    "academic" : "seoul university"
}
{
    "introduction" : "저는 개발자 chan kim 입니다.",
    "skill" : "업데이트 된 이력서",
    "location" : "부산",
    "academic" : "seoul university"
}
```
### job
```
{
    "title" : "job1",
    "content" : "chan kim company에서 백엔드 개발자를 뽑습니다.",
    "position" : "junior",
    "company" : "chan kim company",
    "duty" : "backend software engineer"
}
{
    "title" : "updated title",
    "content" : "chan kim company에서 백엔드 개발자를 뽑습니다.",
    "position" : "senior",
    "company" : "chan kim company",
    "duty" : "backend software engineer"
}
```

## 뷰 설계
```
홈 -> 마이페이지, 커뮤니티, Job
마이페이지 -> 팔로우리스트 & 팔로잉 리스트 이동, 닉네임등록, 이메일 & pw 변경, 회원탈퇴, 이력서로 이동
커뮤니티 홈 -> 베스트 게시글, 게시글 검색, 게시글 작성, 작성자 페이지
게시글 상세 -> 좋아요, 게시글 수정, 게시글 삭제, 댓글 리스트
이력서 -> 이력서 없을시 자동으로 이력서 작성페이지 이동 or 이력서 수정 페이지 이동 가능
채용공고 홈 -> 채용공고 상세, 작성자 프로필로 이동, 채용공고 검색
채용공고 상세 -> 채용 신청, 작성자일 경우 신청자 리스트 확인, 수정, 마감(삭제)
```

## 연관관계
```

```

# 3. 상세 설명
## 팔로우
* 유저들끼리 서로 팔로우가 가능하다.
* 팔로우 엔티티는 팔로우의 주인(팔로우 당하는 사람)인 users와 팔로우를 하는 follower가 있다.
* 하나의 테이블로 팔로잉리스트, 팔로워 리스트를 모두 꺼내올 수 있다.
* 팔로우 1개(detail)도 조회가 가능한데, where and 쿼리를 사용해서 가져온다.
* 팔로우 detail 조회는 팔로잉을 하거나 언팔로잉 시 해당 팔로잉이 이미 존재하는지 여부를 파악하는 데에 쓰인다.
* 쿼리는 아래와 같다.
```
@Query("select f from Follow f join fetch f.follower join fetch f.users where f.follower = :follower and f.users = :users")
```
## Resume
* my-resume로 들어올경우 나의 이력서가 없으면 이력서 post 페이지로 리다이렉트시킴.
* resume에서 introduction같은 경우에는 긴글(설명글)이기에 
* @Column(columnDefinition = "TEXT") 으로 처리를 하였다.
* 그런데 skill의 경우에는 콤마(,) 라던지, 느낌표, 괄호 등의 기호가 들어간다.
* 가지고 있는 기술이 한가지가 아니라 여러개일 수 있기때문이다.
* location 또한 마찬가지라서 컬럼 설정값을 넣었다.
* 예시 : skill : jpa, spring(spring boot)

# 4. 나의 고민


-문서 할일
뷰설계 계속 채워나가기
연관관계
erd 작성

-순서
북마크, 어플라이 + jobservice에 volunteer update 하기.