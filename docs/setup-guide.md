# 프로젝트 상세 설정 가이드

이 문서는 Spring Boot 백엔드 애플리케이션을 Keycloak 및 Docker 환경과 함께 설정하는 과정을 상세히 안내합니다.

## 목차

- [프로젝트 상세 설정 가이드](#프로젝트-상세-설정-가이드)
  - [목차](#목차)
    - [1. Docker 환경 설정](#1-docker-환경-설정)
    - [2. Keycloak 서버 설정](#2-keycloak-서버-설정)
      - [2.1. 백엔드 클라이언트 생성](#21-백엔드-클라이언트-생성)
    - [3. Spring Boot 애플리케이션 설정](#3-spring-boot-애플리케이션-설정)
    - [4. 애플리케이션 실행](#4-애플리케이션-실행)

---

### 1. Docker 환경 설정

이 백엔드 애플리케이션은 Docker 기반의 Keycloak 및 PostgreSQL 서버와 통신합니다. 프론트엔드 프로젝트에서 사용하던 `docker-compose.yml` 파일을 이 프로젝트의 루트 디렉터리에 복사하거나 새로 생성합니다.

---

### 2. Keycloak 서버 설정

Docker 컨테이너가 실행되면 Keycloak 관리자 콘솔에 접속하여 Realm과 Client를 설정해야 합니다.

-   **관리자 콘솔 접속**: `http://localhost:8080`
-   **계정**: `admin` / `admin`


#### 2.1. 백엔드 클라이언트 생성

이 클라이언트는 Spring Boot 애플리케이션이 Keycloak Admin API와 통신하여 사용자 정보를 관리(예: 회원 탈퇴)하는 데 사용됩니다.

1.  왼쪽 메뉴에서 `Clients`로 이동하여 `Create client` 버튼을 클릭합니다.
2.  `Client ID`에 `backend-client`를 입력하고 `Next`를 누릅니다.
3.  **Capability config** 단계에서 다음을 설정합니다.
    -   `Client authentication`: **On**
    -   `Authorization`: **Off**
    -   `Authentication flow`: 모두 **Unchecked**
4.  `Save` 버튼을 눌러 클라이언트를 생성합니다.
5.  생성된 `backend-client` 설정 화면에서 다음을 설정합니다.
    -   `Access type`: **confidential** 로 변경
    -   `Service accounts roles` 탭으로 이동합니다.
    -   `Assign role` 버튼을 클릭합니다.
    -   `Filter by realm roles`를 선택하고 `delete-user`를 검색하여 `realm-management` 클라이언트의 `delete-user` 역할을 할당합니다.
6.  `Credentials` 탭으로 이동하여 `Client secret` 값을 복사해둡니다. 이 값은 Spring Boot 애플리케이션의 `application.properties` 파일에 사용됩니다.
---

### 3. Spring Boot 애플리케이션 설정

`src/main/resources/application.properties` 파일을 열고 다음 내용으로 수정하거나 추가합니다.

```properties
# ===============================================================
# = SERVER CONFIG
# ===============================================================
# Keycloak이 8080 포트를 사용하므로, 충돌을 피하기 위해 8081로 설정
server.port=8081

# ===============================================================
# = KEYCLOAK SPRING SECURITY ADAPTER
# ===============================================================
keycloak.realm=demo
keycloak.auth-server-url=http://localhost:8080
keycloak.resource=frontend-client # 사용자 인증 주체는 프론트엔드 클라이언트
keycloak.public-client=true

# ===============================================================
# = KEYCLOAK ADMIN CLIENT (회원 탈퇴 기능용)
# ===============================================================
keycloak.admin.server-url=http://localhost:8080
keycloak.admin.realm=master # Admin API는 master Realm을 통해 접근
keycloak.admin.username=admin # Keycloak 관리자 계정
keycloak.admin.password=admin # Keycloak 관리자 비밀번호
# 위 관리자 계정 대신, 서비스 계정을 사용하는 것이 보안상 더 안전합니다.
# keycloak.admin.realm=demo
# keycloak.admin.client-id=backend-client
# keycloak.admin.client-secret=YOUR_BACKEND_CLIENT_SECRET # 2.2 단계에서 복사한 Secret 값
```

**[중요]** `YOUR_BACKEND_CLIENT_SECRET` 부분은 **2.1 단계**에서 복사한 `backend-client`의 실제 Secret 값으로 반드시 교체해야 합니다.

---

### 4. 애플리케이션 실행

모든 설정이 완료되면, IDE에서 Spring Boot 애플리케이션을 실행하거나 터미널에서 다음 명령어를 실행합니다.

```bash
./gradlew bootRun
```

애플리케이션이 정상적으로 실행되면 `localhost:8081`에서 API 서버가 동작합니다.
