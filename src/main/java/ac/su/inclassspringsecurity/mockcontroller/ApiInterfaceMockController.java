package ac.su.inclassspringsecurity.mockcontroller;

import ac.su.inclassspringsecurity.constant.UserRole;
import ac.su.inclassspringsecurity.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apiMock")
public class ApiInterfaceMockController {
    // 각 페이지 필요한 데이터 DTO 선언

    // 각 페이지 필요한 URL 맵핑 핸들러 선언
    @ResponseBody
    @GetMapping("/users/{id}")
    // Mock URL : /apiMock/users/{id}
    // 실제 URL : /api/users/{id}
    public ResponseEntity<User> getUser(
        // Mocking 이기 때문에 데이터 저장 또는 조회 없이 바로 보내도 된다.
        @RequestParam(value = "id", required = false, defaultValue = "0") long id
    ) {
        // 더미 유저 객체 생성 후 리턴
        User userDummy = new User();
        userDummy.setId(id);
        userDummy.setUsername("user" + id);
        userDummy.setPassword("password" + id);
        userDummy.setEmail("user" + id + "@tt.cc");
        userDummy.setRole(UserRole.USER);
        return ResponseEntity.ok(userDummy);
    }

    // 위와 같이 객체 하나 짜리 간단한 EndPoint 는 사실
    // Mocking 없이 바로 구현 하는게 낫다고 생각할 수도 있다
    // Admin 페이지, 주문 결과 등 구현에 시간이 걸리는 페이지 들의 경우
    // 당장 interface 합의를 위해서 DTO 를 선언 후
    // Mocking Endpoint 를 만들면 좋다.
}
