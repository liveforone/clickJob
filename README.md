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
* 뷰에 보여주는 모든 유저는 이메일이 아닌 닉네임이다. 이메일은 오직 작성자 검증에서만 사용한다.

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

## 뷰 설계
```
홈 -> 마이페이지, 커뮤니티, Job
마이페이지 -> 팔로우리스트 & 팔로잉 리스트, 닉네임등록, 이메일 & pw 변경, 회원탈퇴
커뮤니티 홈 -> 베스트 게시글, 게시글 검색
```

## 연관관계
```

```

# 3. 상세 설명

# 4. 나의 고민


-문서 할일
뷰설계 계속 채워나가기

-코드 할일
job에 좋아요(해쉬태그는 따로 않넣어도 됨.)
북마크 추가(job에만)
팔로우 넣기(인맥)

-순서
팔로우, 레줘미, 잡, 북마크, 어플라이, 
전체적인 서비스로직에서 dto detail로 변환시 널 체크하고 널인경우 널 반환
무언가 반환할때 null이다. 그럼 null 리턴하면 됨.

-주의사항
코드를 작성하며 중유하거나 새롭게 적용한점은 바로 상세설명란에 문서화 시킨다.
문서화는 바로바로 하는 것을 지향한다.

팔로우 중복체크
잡에서 타이틀과 컨텐트는 size 걸지말자.