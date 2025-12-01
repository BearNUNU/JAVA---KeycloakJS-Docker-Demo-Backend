package com.example.demo.service;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 사용자 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * Keycloak Admin Client를 사용하여 Keycloak의 사용자를 관리합니다.
 */
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final Keycloak keycloak;
    private final String realm;

    /**
     * UserService 생성자입니다.
     * Keycloak 클라이언트와 Realm 이름을 주입받습니다.
     * @param keycloak Keycloak Admin Client 인스턴스
     * @param realm    application.properties에서 주입된 Realm 이름
     */
    public UserService(Keycloak keycloak, @Value("${keycloak.admin.realm}") String realm) {
        this.keycloak = keycloak;
        this.realm = realm;
    }

    /**
     * Keycloak에서 특정 사용자 ID를 가진 사용자를 삭제합니다.
     *
     * @param userId 삭제할 사용자의 ID (UUID 형식)
     */
    public void deleteUser(String userId) {
        log.info("사용자 삭제 시도 - ID: {}", userId);
        try {
            // 1. Realm 리소스 및 Users 리소스 가져오기
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();
            log.debug("Keycloak Realm '{}'에 대한 리소스를 가져왔습니다.", realm);

            // 2. 사용자 삭제 API 호출
            // Keycloak Admin Client는 내부적으로 토큰을 관리하므로 직접 토큰을 추가할 필요가 없습니다.
            log.info("Keycloak 사용자 삭제 API 호출: DELETE /admin/realms/{}/users/{}", realm, userId);
            try (Response response = usersResource.delete(userId)) {

                // 3. 결과 확인
                if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                    log.info("Keycloak에서 사용자 '{}'를 성공적으로 삭제했습니다. 상태 코드: {}", userId, response.getStatus());
                } else {
                    // 오류 응답 본문을 읽기 위해 추가
                    String errorBody = response.readEntity(String.class);
                    log.error("Keycloak에서 사용자 '{}' 삭제 실패. 상태 코드: {}, 응답: {}", userId, response.getStatus(), errorBody);
                    throw new RuntimeException("Keycloak 사용자 삭제 실패. 상태 코드: " + response.getStatus());
                }
            }

        } catch (Exception e) {
            log.error("Keycloak에서 사용자 '{}' 삭제 중 예외 발생.", userId, e);
            // 클라이언트 측에서 발생한 예외(예: 연결 실패) 또는 위에서 던진 RuntimeException을 처리합니다.
            throw new RuntimeException("Keycloak 통신 중 오류가 발생했습니다.", e);
        }
    }
}
