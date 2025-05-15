# 바로인턴 12기 과제


## 개요

Java 17 기반의 Spring Boot 웹 애플리케이션으로, 회원가입, 로그인, 권한 변경 등 기본적인 인증/인가 기능을 구현한 과제 프로젝트입니다.

## 주요 기능 및 특징

* **JWT 인증 기반 사용자 인증/인가 기능**
* **Admin 권한으로 사용자 권한(Role) 변경 가능**
* **Swagger UI를 통한 API 문서 자동화**
* **Jacoco를 통한 테스트 커버리지 리포트 자동 생성 및 배포 (Coverage 80% 이상)**
* **Github Actions 기반 CI 구성 (테스트 자동화)**
* **향후 Blue-Green 무중단 배포를 위한 Nginx Reverse Proxy 적용**
* **Github Projects를 활용한 Issue 생성 및 작업 관리**

## 기술 스택

| 항목         | 사용 기술                       |
| ---------- |-----------------------------|
| Language   | Java 17                     |
| Framework  | Spring Boot 3               |
| Build Tool | Gradle                      |
| Database   | H2 (In-Memory)              |
| ORM        | SpringData JPA              |
| Test       | JUnit 5 + Jacoco            |
| CI/CD      | GitHub Actions              |
| API Docs   | Swagger / Springdoc OpenAPI |



## 프로젝트 구조

```text
internassignment
├─ application          # 유스케이스, 서비스 로직
├─ common               # 공통 예외
├─ domain               # 도메인 모델, 인터페이스
├─ infrastructure       # 설정, 보안, JWT
├─ presentation         # Controller, DTO
└─ InternAssignmentApplication.java
```


## 실행 방법

### 1. 프로젝트 클론

```bash
git clone https://github.com/challduck/baro_intern_12_assignment.git
cd baro_intern_12_assignment
```

### 2. 애플리케이션 실행

```bash
./gradlew build
./gradlew bootRun
```


## API 문서 & 테스트 리포트

* **Test Coverage (Jacoco)**: [Coverage Report](https://challduck.github.io/baro_intern_12_assignment/)
* **Swagger UI (API 명세)**: [Swagger API Docs](http://3.35.136.127:8080/swagger-ui/index.html)


## 주요 테스트 전략

* Controller, Service Layer 단위 테스트 구성
* 예외 케이스 및 인증 실패 케이스 테스트 포함
* Coverage 80% 이상 달성

## 핵심 API 예시

| HTTP Method | Endpoint                      | 설명           |
| ----------- | ----------------------------- | ------------ |
| `POST`      | `/signup`                     | 사용자 회원가입     |
| `POST`      | `/admin/signup`               | 관리자 회원가입     |
| `POST`      | `/login`                      | 로그인 (JWT 발급) |
| `PATCH`     | `/admin/users/{userId}/roles` | 사용자 권한 변경    |


## 제출 정보

* **Swagger UI 주소**: [http://3.35.136.127:8080/swagger-ui/index.html](http://3.35.136.127:8080/swagger-ui/index.html)
* **테스트 커버리지 리포트 (GitHub Pages)**: [https://challduck.github.io/baro\_intern\_12\_assignment/](https://challduck.github.io/baro_intern_12_assignment/)
