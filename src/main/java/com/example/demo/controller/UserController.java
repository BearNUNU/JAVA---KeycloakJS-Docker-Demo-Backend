package com.example.demo.controller;

import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 사용자 관련 API 요청을 처리하는 컨트롤러 클래스입니다.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    // 로깅을 위한 Logger 객체 생성
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    // 사용자 관련 비즈니스 로직을 처리하는 서비스
    private final UserService userService;

    // 생성자를 통해 UserService 의존성을 주입받습니다.
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 현재 인증된 사용자의 계정을 Keycloak에서 삭제합니다.
     * 사용자는 JWT 액세스 토큰의 'sub' 클레임으로 식별됩니다.
     *
     * @param jwt Spring Security가 주입해주는 인증된 사용자의 JWT 토큰
     * @return 계정 삭제 성공 여부를 담은 응답 엔티티
     */
    @DeleteMapping("/me")
    @PreAuthorize("isAuthenticated()") // 이 엔드포인트는 인증된 사용자만 호출할 수 있습니다.
    public ResponseEntity<?> deleteCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        // JWT에서 사용자 ID('sub')와 사용자 이름('preferred_username')을 추출합니다.
        String userId = jwt.getSubject();
        String preferredUsername = jwt.getClaimAsString("preferred_username");
        
        log.info("사용자 계정 삭제 요청 수신 - 사용자 ID: {}, 사용자 이름: {}", userId, preferredUsername);

        // UserService를 통해 사용자 삭제 로직을 호출합니다.
        userService.deleteUser(userId);

        log.info("사용자 계정 삭제 처리 완료 - 사용자 ID: {}, 사용자 이름: {}", userId, preferredUsername);
        
        // 클라이언트에게 성공 메시지를 반환합니다.
        return ResponseEntity.ok(Map.of("message", "사용자 '" + preferredUsername + "'의 계정이 성공적으로 삭제되었습니다."));
    }
}
