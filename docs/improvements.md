# 프로젝트 개선 제안서

본 문서는 현재 프로젝트의 분석을 바탕으로 식별된 개선점과 제안 사항을 정리합니다.

## 1. 보안 (Security)

### 1.1 하드코딩된 민감 정보 관리
- **문제점**: `application.properties` 및 가이드 문서에 Keycloak 관리자 계정(`admin`/`admin`)과 같은 민감한 정보가 하드코딩되어 있거나 예시로 사용되고 있습니다.
- **개선안**:
  - `application.properties`에서 민감한 정보를 환경 변수(Environment Variables)로 대체합니다.
  - 예: `keycloak.admin.password=${KEYCLOAK_ADMIN_PASSWORD}`
  - `.env` 파일을 지원하거나, 실행 시 환경 변수를 주입하는 방식을 가이드해야 합니다.

### 1.2 서비스 계정 사용 강제
- **문제점**: 현재 설정은 `master` 렐름의 `admin` 계정을 사용하여 사용자 삭제 기능을 수행하도록 구성되어 있을 수 있습니다 (주석 처리된 부분 등). 이는 보안상 매우 취약합니다.
- **개선안**:
  - `backend-client` (Service Account)를 사용하는 구성을 기본값으로 설정합니다.
  - `setup-guide.md`에서 `admin` 계정 사용 방법을 제거하고, 서비스 계정 설정 방법만을 안내합니다.

## 2. 개발 및 배포 환경 (DevOps)

### 2.1 Docker Compose 파일 내장
- **문제점**: `README.md`에서 "프론트엔드 프로젝트의 `docker-compose.yml`을 복사하라"고 안내하고 있어, 백엔드 프로젝트 단독으로 실행 및 테스트하기 어렵습니다.
- **개선안**:
  - 프로젝트 루트에 백엔드 개발에 필요한 Keycloak 및 PostgreSQL 구성을 포함한 `docker-compose.yml` 파일을 직접 포함시킵니다.
  - 이를 통해 `docker-compose up` 명령 하나로 로컬 개발 환경을 즉시 구축할 수 있도록 합니다.

### 2.2 CI/CD 파이프라인 부재
- **문제점**: 현재 자동화된 빌드, 테스트, 배포를 위한 파이프라인 구성이 확인되지 않습니다.
- **개선안**:
  - GitHub Actions 등을 활용한 기본적인 CI 파이프라인(빌드 및 테스트 자동화)을 구축합니다.

## 3. 코드 품질 및 테스트 (Quality Assurance)

### 3.1 테스트 코드 부족
- **문제점**: `src/test` 디렉토리에 기본적인 `DemoApplicationTests` 외에 유닛 테스트나 통합 테스트가 부족해 보입니다.
- **개선안**:
  - 주요 비즈니스 로직(예: `UserService`)에 대한 유닛 테스트를 작성합니다.
  - Testcontainers 등을 활용하여 Keycloak과 연동되는 통합 테스트 환경을 구축하면 안정성을 높일 수 있습니다.

### 3.2 에러 처리 (Error Handling)
- **문제점**: Keycloak API 호출 실패 등에 대한 구체적인 에러 처리 전략이 명시되어 있지 않습니다.
- **개선안**:
  - 전역 예외 처리기(`@ControllerAdvice`)를 도입하여 일관된 에러 응답 포맷을 정의합니다.
  - Keycloak 연동 실패 시 사용자에게 적절한 메시지를 전달하도록 개선합니다.

## 4. 문서화 (Documentation)

### 4.1 API 문서화
- **문제점**: API에 대한 명세가 소스 코드나 별도 문서로만 존재하여, 프론트엔드 개발자나 외부 연동 시 확인이 어렵습니다.
- **개선안**:
  - Swagger (SpringDoc OpenAPI)를 도입하여 API 문서를 자동화하고, `http://localhost:8081/swagger-ui.html` 등에서 테스트해볼 수 있도록 합니다.

### 4.2 아키텍처 다이어그램
- **문제점**: 시스템의 전체적인 구조(Spring Boot <-> Keycloak <-> DB)를 한눈에 파악할 수 있는 시각적 자료가 부족합니다.
- **개선안**:
  - `README.md`에 시스템 아키텍처 다이어그램을 추가하여 이해를 돕습니다.
