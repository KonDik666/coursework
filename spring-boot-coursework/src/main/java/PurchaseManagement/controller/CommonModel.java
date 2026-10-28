package PurchaseManagement.controller;

import PurchaseManagement.service.AccessService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CommonModel {
    private final AccessService access;
    public CommonModel(AccessService access) { this.access = access; }
    @ModelAttribute
    public void common(Model model, Authentication authentication) {
        boolean signedIn = authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
        model.addAttribute("signedIn", signedIn);
        model.addAttribute("isAdmin", signedIn && access.isAdmin());
        model.addAttribute("isWriter", signedIn && access.isWriter());
        model.addAttribute("ownRecordsOnly", signedIn && access.ownRecordsOnly());
        model.addAttribute("loginName", signedIn ? authentication.getName() : "");
    }
}
