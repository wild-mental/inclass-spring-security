package ac.su.inclassspringsecurity.apicontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api-mock")
public class ApiMockController {
    @GetMapping
    public ResponseEntity<String> getApiMock() {
        return new ResponseEntity<>(
            "Calling API root URL is not allowed.",
            HttpStatus.FORBIDDEN);
    }

    @GetMapping("/health-check")
    public String getApiMockTest() {
        return "API Mock Health Check Success!";
    }

    @GetMapping("/make-dummy/all")
    public String makeDummyAll() {
        makeDummyUsers();
        makeDummyProducts();
        makeDummyCarts();
        makeDummyOrders();
        return "All Dummy Data Created! (Users, Products, Carts, Orders)";
    }

    @GetMapping("/make-dummy/users")
    public String makeDummyUsers() {
        // TODO : Dummy Users 생성 로직 추가
        return "Dummy Users Created!";
    }

    @GetMapping("/make-dummy/products")
    public String makeDummyProducts() {
        // TODO : Dummy Products 생성 로직 추가
        return "Dummy Products Created!";
    }

    @GetMapping("/make-dummy/carts")
    public String makeDummyCarts() {
        // TODO : Dummy Carts 생성 로직 추가
        return "Dummy Carts Created!";
    }

    @GetMapping("/make-dummy/orders")
    public String makeDummyOrders() {
        // TODO : Dummy Orders 생성 로직 추가
        return "Dummy Orders Created!";
    }
}
