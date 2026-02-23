# 🎮 RP Streamer Platform

## 📌 프로젝트 소개

스트리머 RP 콘텐츠를 보다 쉽게 탐색하고 시청할 수 있도록 제작한 웹 서비스입니다.
사용자는 인기 영상 확인, 스트리머별 영상 모아보기, 좋아요 기능 등을 통해 원하는 콘텐츠를 빠르게 찾을 수 있습니다.

---

## 🛠 기술 스택

### Backend

* Java
* Spring MVC
* MyBatis
* JSP / JSTL

### Frontend

* HTML
* CSS
* JavaScript

### Database

* MySQL

### External API

* Kakao Login API
* YouTube Data API

### Tools

* Eclipse
* Git / GitHub
* Apache Tomcat

---

## 🚀 주요 기능

### ✔ 메인 페이지

* 조회수 기준 인기 영상 정렬
* 상위 영상 카드 UI 제공
* 영상 클릭 시 재생 페이지 이동

### ✔ 영상 재생 페이지

* YouTube 영상 재생
* 조회수 증가 로직 (세션 기준 중복 방지)
* 스트리머 영상 모아보기 기능

### ✔ 스트리머 목록

* 스트리머별 영상 탐색
* 좋아요 기능 제공

### ✔ 카카오 로그인

* OAuth2 기반 소셜 로그인
* 사용자 정보 자동 연동

### ✔ 마이페이지

* 좋아요 스트리머 관리
* 사용자 활동 확인

---

## 📂 프로젝트 구조

```
src
 ├── controller
 ├── service
 ├── dao
 ├── dto
 └── views(JSP)
```

Spring MVC 구조 기반으로 Controller / Service / DAO 계층을 분리하여 설계했습니다.

---

## ⚙️ 실행 방법

### 1️⃣ 프로젝트 클론

```
git clone https://github.com/본인아이디/repository.git
```

### 2️⃣ 설정 파일 생성

보안상의 이유로 `application.properties` 파일은 저장소에 포함되어 있지 않습니다.

`src/main/resources` 경로에서:

```
application-example.properties
```

파일을 복사 후

```
application.properties
```

로 이름을 변경하여 사용하세요.

### 3️⃣ DB 설정

* MySQL DB 생성
* datasource 정보 입력

### 4️⃣ 실행

Tomcat 서버 실행 후 접속

```
http://localhost:8081
```

---

## 🔒 보안 처리

* API Key 및 민감 정보는 GitHub에 업로드하지 않도록 `.gitignore` 적용
* example 설정 파일 제공으로 안전한 프로젝트 공유 환경 구성

---

## 📈 프로젝트 목표

단순 기능 구현을 넘어 사용자 흐름과 서비스 구조를 이해하고,
유지보수와 확장성을 고려한 웹 서비스 구현을 목표로 개발했습니다.

---

## 👨‍💻 개발자

* GitHub: https://github.com/dxhickswn95
