# Keycloak 연동 Spring Boot 백엔드 데모

이 프로젝트는 Keycloak과 연동하여 사용자 인증 및 회원 탈퇴 기능을 제공하는 Spring Boot 백엔드 애플리케이션입니다. Docker Compose를 사용하여 Keycloak 및 PostgreSQL 데이터베이스를 함께 실행하는 환경을 기반으로 합니다.

## 🌟 주요 기능

- **사용자 인증**: Keycloak을 통한 JWT 기반의 안전한 사용자 인증을 처리합니다.
- **회원 탈퇴**: Keycloak Admin API와 통신하여 등록된 사용자를 안전하게 삭제합니다.
- **보안 구성**: Spring Security와 Keycloak 어댑터를 사용하여 API 엔드포인트를 보호합니다.

## 🛠️ 기술 스택

- **언어**: Java 21
- **프레임워크**: Spring Boot 3
- **인증**: Keycloak
- **데이터베이스**: PostgreSQL (Keycloak 데이터 저장용)
- **빌드 도구**: Gradle
- **컨테이너**: Docker

## 📝 프로젝트 구성

- `src/main/java`: 애플리케이션의 핵심 로직이 담긴 Java 소스 코드
  - `config`: Keycloak 및 Spring Security 설정 클래스
  - `controller`: API 요청을 처리하는 컨트롤러
  - `service`: 비즈니스 로직을 담당하는 서비스
- `src/main/resources`: `application.properties` 등 리소스 파일
- `build.gradle`: 프로젝트 의존성 및 빌드 설정
- `docs/setup-guide.md`: 상세한 프로젝트 설정 가이드

## 🚀 시작하기

프로젝트를 실행하고 사용하는 방법에 대한 자세한 내용은 `docs/setup-guide.md` 파일을 참고하세요.