package ac.su.inclassspringsecurity.controller;

import ac.su.inclassspringsecurity.domain.Product;
import ac.su.inclassspringsecurity.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/thymeleaf")
public class ProductTempController {
    private final ProductService productService;

    @GetMapping("/ex01")
    public String ex01(Model model) {
        Product product = new Product();
        product.setName("this is sample product");
        model.addAttribute("data", "This is assigned from controller!!");
        model.addAttribute("data1", product);
        model.addAttribute("data2", "second data");
        model.addAttribute("data3", "third data");
        return "thymeleaf/ex01";
    }
}
