package ac.su.inclassspringsecurity.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class CSRFTokenController {

    @GetMapping("/csrf-token")
    public CsrfToken csrfToken(CsrfToken csrfToken) {
        return csrfToken;
    }
//    @PostMapping("/submit")
//    public Map<String, ?> handlePostRequest(@RequestBody String body, @RequestHeader("X-CSRF-TOKEN") String csrfToken, HttpServletRequest request) {
//        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
//        Map<String, ?> resData;
//        if (token != null && token.getToken().equals(csrfToken)) {
//            // CSRF 토큰이 유효한 경우 처리 로직
//            resData = Map.of(
//                "resCode", 200,
//                "resMsg",  "Request processed successfully",
//                "tokenReceived", csrfToken,
//                "tokenFromRequest", token.getToken()
//            );
//        } else {
//            // CSRF 토큰이 유효하지 않은 경우 처리 로직
//            resData = Map.of(
//                "resCode", 403,
//                "resMsg",  "CSRF token validation failed",
//                "tokenReceived", csrfToken,
//                "tokenFromRequest", (token!=null ? token.getToken() : "null token")
//            );
//        }
//        return resData;
//    }
    @PostMapping("/submit")
    public Map<String, ?> handlePostRequest(@RequestBody String body, @RequestHeader("X-CSRF-TOKEN") String csrfToken, HttpServletRequest request) {
        // X-XSRF-TOKEN 헤더 참조 필요 (spring security 기본 설정)
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        // 현재 코드 라인까지 들어온 경우 CSRF 토큰은 유효한 상태
        Map<String, ?> resData = Map.of(
                "resCode", 200,
                "resMsg",  "CSRF token validation automatically handled by Spring Security",
                "tokenReceived", csrfToken,
                "tokenFromRequest", (token!=null ? token.getToken() : "null token")
        );
        return resData;
    }
}