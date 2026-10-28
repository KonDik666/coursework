package PurchaseManagement.controller;

import PurchaseManagement.dto.CalculationDto;
import PurchaseManagement.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.math.BigDecimal;

@Slf4j
@Controller
public class CalculationController {
    private final CalculationService calculations;
    private final ProductService products;
    private final AccessService access;
    public CalculationController(CalculationService calculations, ProductService products, AccessService access) {
        this.calculations = calculations; this.products = products; this.access = access;
    }
    @GetMapping("/calculator")
    public String form(Model model) {
        model.addAttribute("calculation", new CalculationDto()); model.addAttribute("total", products.total());
        return "calculator";
    }
    @PostMapping("/calculator")
    public String calculate(@Valid @ModelAttribute("calculation") CalculationDto dto, BindingResult result, Model model) {
        BigDecimal total = products.total();
        model.addAttribute("total", total);
        if (!result.hasErrors()) {
            BigDecimal remaining = calculations.calculate(dto, total);
            model.addAttribute("remaining", remaining);
            model.addAttribute("difference", remaining.abs());
            model.addAttribute("usedPercent", calculations.usedPercent(dto, total));
            log.info("Пользователь {} выполнил проверку бюджета", access.currentUser().getUsername());
        }
        return "calculator";
    }
}
