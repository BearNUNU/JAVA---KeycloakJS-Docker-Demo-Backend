package com.example.demo.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;

/**
 * 사용자 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * Keycloak Admin Client를 사용하여 사용자를 관리합니다.
 */
@Service
public class UserService {

    // 로깅을 위한 Logger 객체 생성
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    // Keycloak Admin Client 인스턴스
    private final Keycloak keycloak;
    
    // application.properties에서 설정한 Keycloak realm 이름
    private final String realm;

    /**
     * 생성자를 통해 Keycloak Admin Client와 realm 이름을 주입받습니다.
     * @param keycloak KeycloakConfig에서 생성된 Keycloak 빈
     * @param realm    application.properties의 'keycloak.admin.realm' 값
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
            // 설정된 realm의 사용자 리소스를 가져옵니다.
            UsersResource usersResource = keycloak.realm(realm).users();
            
            // 사용자 ID를 사용하여 삭제 API를 호출합니다.
            // Response 객체는 try-with-resources 구문을 사용하여 자동으로 닫히도록 합니다.
            try (Response response = usersResource.delete(userId)) {
                int status = response.getStatus();
                // HTTP 상태 코드가 2xx 범위인 경우 성공으로 간주합니다. (예: 204 No Content)
                if (status >= 200 && status < 300) {
                    log.info("Keycloak에서 사용자 '{}'를 성공적으로 삭제했습니다. 상태 코드: {}", userId, status);
                } else {
                    // 삭제 실패 시, 응답 본문을 읽어 에러 로그를 남깁니다.
                    String responseBody = response.readEntity(String.class);
                    log.error("Keycloak에서 사용자 '{}' 삭제 실패. 상태 코드: {}, 응답 본문: {}", userId, status, responseBody);
                    throw new RuntimeException("Keycloak 사용자 삭제 실패. 상태 코드: " + status);
                }
            }
        } catch (Exception e) {
            // Keycloak 통신 중 예외 발생 시 에러 로그를 남깁니다.
            log.error("Keycloak에서 사용자 '{}' 삭제 중 예외 발생.", userId, e);
            throw new RuntimeException("Keycloak 통신 중 오류가 발생했습니다.", e);
        }
    }
}
