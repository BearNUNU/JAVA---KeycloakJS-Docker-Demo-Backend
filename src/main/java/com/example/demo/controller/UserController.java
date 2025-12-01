package com.example.demo.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.UserService;

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
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String id) {

        // 프론트에서 받은 토큰은 사용자가 본인인지 확인 용도
        String loggedInUserId = jwt.getSubject();

        if (!loggedInUserId.equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
