# 이음
## 주제
사용자가 시간적인 제한에서 벗어나 가족과 함께 여행을 할 수 있는 '이음'서비스입니다.

## 개발 기간
2023.04.10 ~ 2023.05.18 (6주)

## 팀원
6명(프론트 3명, 백앤드 2명, 안드로이드 스튜디오 1명)

## 주요 기술 스택

### FrontEnd
Next.js, React.js

### BackEnd
Spring Boot, JPA, QueryDSL, Spring Security Oauth2, MySQL, FastAPI, Android Studio, Media Pipe

### Deploy
Docker, Jenkins, Nginx, AWS CloudFront, AWS S3, AWS Ec2

### 기능
- 사용자는 Spring Security를 이용한 Kakao 소셜 로그인을 할 수 있습니다.
- 사용자는 Group을 만들 수 있습니다.
- 사용자는 Group 코드를 통해 그룹에 참여할 수 있습니다.
- 사용자는 지도에서 이벤트를 등록할 수 있습니다. 
- - 이벤트 : 함께찍기(에이징/디에이징), 동작 따라 찍기가 있습니다.
- Group에 있는 다른 사용자는 등록된 이벤트를 수행할 수 있습니다.
- 함께찍기 이벤트를 수행할 때 등록된 사진과 이벤트를 하는 사용자의 사진을 합성할 수 있습니다.

### 와이어 프레임
![wireframe](./img/wireframe.PNG)

### API 설계
#### User 
![user_api](./img/user_api.PNG)

#### Group
![group_api](./img/group_api.PNG)

#### Pin
![pin_api](./img/pin_api.PNG)
![pin_api2](./img/pin_api2.PNG)

### ERD 설계
![erd_api](./img/erd_api.PNG)


