# clickJob
> 구인 구직 플랫폼. 정규직부터 비정규직까지.

## 기술 스택
* Language : Java17
* DB : MySql
* ORM : Spring Data Jpa
* Spring Security
* LomBok
* Gradle
* Spring Boot 2.7.5
* Apache commons lang3

# 1. 설명
* 일을 잡는 구직 플랫폼이다. 링크드인에서 많이 영감을 얻었다.
* 구인 구직도 하면서 본인의 생각을 작성할 수 있는 게시판도 있다.
* 단순히 구직만 하는 플랫폼을 넘어 소통의 기능까지 겸비한 플랫폼이다.
* rest-api server

# 2. 설계


# 3. 상세 설명

# 4. 나의 고민

-문서 할일


-코드 할일
유저에 닉네임 넣기. 전체 구조는 breve 껄로
전체적인 서비스로직에서 dto detail로 변환시 널 체크하고 널인경우 널 반환
job에 좋아요(해쉬태그는 따로 않넣어도 됨.)
board에 좋아요, 조횟수 넣기
북마크 추가(job에만)
팔로우 넣기(인맥)
게시글은 인맥으로 긁어오기(생각을 깊이 해봐야함.쿼리를)
게시글은 검색 불가능하게 함. 검색은 오로지 job만(여러 조건)

-순서
유저, 레줘미, 잡, 북마크, 어플라이, 보드, 팔로우
파일 업로드는 없음.