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
![스크린샷(141)](https://user-images.githubusercontent.com/88976237/202464309-68163a71-f0dd-490f-8dea-310051f64ebb.png)

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
### bookmark
```
/my-bookmark - get
/bookmark/post/{jobId} - post
/bookmark/cancel/{jobId} - post
```
### apply
```
/apply-list/{jobId} - get
/my-apply - get
/apply/{jobId} - post
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
마이페이지 -> 팔로우리스트 & 팔로잉 리스트 이동, 닉네임등록, 이메일 & pw 변경, 회원탈퇴, 이력서로 이동, 나의 구직 리스트, 나의 북마크
커뮤니티 홈 -> 베스트 게시글, 게시글 검색, 게시글 작성, 작성자 페이지
게시글 상세 -> 좋아요, 게시글 수정, 게시글 삭제, 댓글 리스트
이력서 -> 이력서 없을시 자동으로 이력서 작성페이지 이동 or 이력서 수정 페이지 이동 가능
채용공고 홈 -> 채용공고 상세, 작성자 프로필로 이동, 채용공고 검색
채용공고 상세 -> 채용 신청, 작성자일 경우 신청자 리스트 확인, 수정, 마감(삭제)
```

## 연관관계
```
resume -> users(다대일)
job -> users(다대일)
follow -> users(users, follower, 다대일, 이중조인)
bookmark -> users(다대일), job(다대일)
apply -> users(다대일), job(다대일)
board -> users(다대일)
comment -> users(다대일), board(다대일)
```

# 3. 상세 설명
## 유저 널체크 안하는 이유
* 객체에 대한 널체킹을 진행하는데 유저에 대한 널체킹을 안하는 이유가 궁금할 수 있다.
* 그 이유는 SecurityConfig에서 url에 진입할때 authenticated 즉 인증받은
* 로그인한 사용자만 접근 가능하도록 설정해놓았기 때문에 인증을 받지 못한 사람은
* 인증 받지 못해서 진입을 못한다는 문구가 뜨며 차단당한다.
* 따라서 해당 url에 진입할 수 있는 사람이라면 이미 회원가입/로그인이 끝난 사람이기에 
* 회원정보를 가져오는데 무리가 없다. 당연히 null도 아니고 말이다.
* 따라서 유저는 널체크를 하지 않고 오로지 객체만 null을 체크한다.
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
## 신청서비스 중복 체크
* 해당 어플리케이션에는 여러가지 신청 서비스가 있다.
* 북마크, 팔로우, 구직(apply)
* 위와 같은 신청 서비스는 중복이 일어난다.
* 따라서 반드시 where and 절로 detail을 db에서 찾아와 중복을 체크해주는 것이 필요하다.
* 쿼리 예시는 [여기](#팔로우)에 있다.

# 4. 나의 고민
## if문의 복잡성 줄이기
* 컨트롤러에서 null-check을 하거나, 수정, 삭제 같은 경우 작성자와 현재 유저가 맞는지 판별하는 로직이 많다.
* if - else if - else 로 가독성이 최악이다.
* 또한 너무 난잡하다. 이러한 문제를 어떻게 해결할까 고민을 해보았더니 gate way style 이라는 해답이 존재했다.
* if 문은 아주 간단하게 사용할 수 있는 강력한 분기문인데,
* 이 if문을 그대로 사용하며 가독성을 정말 너무너무 좋게 만들 수 있는 스타일이다.
* 이것에 완전히 흠뻑 빠져버렸고, 그 스타일에 대해 내가 정리를 한 링크를 아래에 달았다.
* [gate way 스타일 가이드](https://github.com/liveforone/study/blob/main/GoodCode/%EB%8D%94%20%EC%A2%8B%EC%9D%80%20%EB%B6%84%EA%B8%B0%EB%AC%B8.md)

# 5. 새롭게 추가한점
* dto -> entity 메소드 서비스로직으로 이동 후 서비스 로직에서 처리
* 반복되는 entity -> dto builder 를 함수화 해서 불필요한 반복을 줄임.
* [긴함수나 변수 줄바꿈으로 가독성 향상](https://github.com/liveforone/study/blob/main/GoodCode/%EC%A4%84%EB%B0%94%EA%BF%88%EC%9C%BC%EB%A1%9C%20%EA%B0%80%EB%8F%85%EC%84%B1%20%ED%96%A5%EC%83%81.md)