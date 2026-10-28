package PurchaseManagement.controller;

import PurchaseManagement.dto.ShopDto;
import PurchaseManagement.entity.Shop;
import PurchaseManagement.service.AccessService;
import PurchaseManagement.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;

@Slf4j
@Controller
public class ShopController {
    private final ShopService shops;
    private final AccessService access;
    public ShopController(ShopService shops, AccessService access) { this.shops = shops; this.access = access; }
    @GetMapping("/shops")
    public String list(Model model) {
        model.addAttribute("shops", shops.list());
        log.info("Пользователь {} открыл список магазинов", access.currentUser().getUsername());
        return "list-shops";
    }
    @GetMapping("/addShopForm")
    public String add(Model model) { access.requireWrite(); model.addAttribute("shop", new ShopDto()); return "add-shop-form"; }
    @GetMapping("/showShopUpdateForm")
    public String edit(@RequestParam Long shopId, Model model) {
        Shop shop = shops.editable(shopId);
        ShopDto dto = new ShopDto(); dto.setId(shop.getId()); dto.setName(shop.getName()); dto.setAddress(shop.getAddress());
        model.addAttribute("shop", dto); return "add-shop-form";
    }
    @PostMapping("/saveShop")
    public String save(@Valid @ModelAttribute("shop") ShopDto dto, BindingResult result, RedirectAttributes flash) {
        access.requireWrite();
        if (dto.getId() != null) shops.editable(dto.getId());
        if (result.hasErrors()) return "add-shop-form";
        shops.save(dto); flash.addFlashAttribute("message", "Магазин сохранён"); return "redirect:/shops";
    }
    @PostMapping("/deleteShop")
    public String delete(@RequestParam Long shopId, RedirectAttributes flash) {
        try { shops.delete(shopId); flash.addFlashAttribute("message", "Магазин удалён"); }
        catch (IllegalArgumentException e) { flash.addFlashAttribute("errorMessage", e.getMessage()); }
        return "redirect:/shops";
    }
}
