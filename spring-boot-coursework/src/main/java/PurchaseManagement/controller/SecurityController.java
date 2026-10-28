package PurchaseManagement.controller;

import PurchaseManagement.dto.UserDto;
import PurchaseManagement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;

@Controller
public class SecurityController {
    private final UserService users;
    public SecurityController(UserService users) { this.users = users; }
    @GetMapping({"/", "/index"})
    public String index() { return "index"; }
    @GetMapping("/about")
    public String about() { return "about"; }
    @GetMapping("/login")
    public String login() { return "login"; }
    @GetMapping("/register")
    public String register(Model model) { model.addAttribute("user", new UserDto()); return "register"; }
    @PostMapping("/register/save")
    public String save(@Valid @ModelAttribute("user") UserDto dto, BindingResult result) {
        if (users.findByUsername(dto.getUsername()) != null) result.rejectValue("username", null, "Этот логин уже занят");
        if (result.hasErrors()) return "register";
        try { users.saveUser(dto); }
        catch (IllegalArgumentException e) { result.rejectValue("username", null, e.getMessage()); return "register"; }
        return "redirect:/register?success";
    }
    @GetMapping("/users")
    public String users(Model model) { model.addAttribute("users", users.findAllUsers()); return "users"; }
    @PostMapping("/users/{id}/roles")
    public String role(@PathVariable Long id, @RequestParam String role, RedirectAttributes flash) {
        users.addRole(id, role);
        flash.addFlashAttribute("message", "Роль добавлена. Пользователю нужно выйти и войти снова, чтобы обновить сеанс.");
        return "redirect:/users";
    }
}
