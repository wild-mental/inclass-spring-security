package ac.su.inclassspringsecurity.service;

import org.springframework.stereotype.Service;

@Service
public class MainPageService {
    // 리포지토리를 주입받기보다, 구현된 low level 서비스를 주입받아서 사용하는 것이 더 좋다.
    public String getMainPage() {
        return "index";
    }
}
