package com.example.demo.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Keycloak Admin Client를 설정하고 Spring Bean으로 등록하는 클래스입니다.
 * 이 설정은 Keycloak의 Admin API를 사용하기 위해 필요합니다.
 */
@Configuration
public class KeycloakConfig {

    // application.properties에서 Keycloak 서버 URL을 주입받습니다.
    @Value("${keycloak.admin.server-url}")
    private String serverUrl;

    // application.properties에서 대상 Realm 이름을 주입받습니다.
    @Value("${keycloak.admin.realm}")
    private String realm;

    // application.properties에서 Admin API 접근용 클라이언트 ID를 주입받습니다.
    @Value("${keycloak.admin.client-id}")
    private String clientId;

    // application.properties에서 클라이언트 Secret을 주입받습니다.
    @Value("${keycloak.admin.client-secret}")
    private String clientSecret;

    /**
     * Keycloak Admin Client 인스턴스를 생성하여 Spring의 Bean으로 등록합니다.
     * UserService 등 다른 컴포넌트에서 이 Bean을 주입받아 사용할 수 있습니다.
     * @return 설정이 완료된 Keycloak 인스턴스
     */
    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl) // Keycloak 서버 주소
                .realm(realm)         // 대상 Realm
                .grantType("client_credentials") // 클라이언트 자격 증명 방식 사용
                .clientId(clientId)   // 클라이언트 ID
                .clientSecret(clientSecret) // 클라이언트 Secret
                .build(); // Keycloak 인스턴스 생성
    }
}
