package PurchaseManagement.controller;

import PurchaseManagement.dto.ProductDto;
import PurchaseManagement.entity.Product;
import PurchaseManagement.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;

@Slf4j
@Controller
public class ProductController {
    private final ProductService products;
    private final ShopService shops;
    private final AccessService access;
    public ProductController(ProductService products, ShopService shops, AccessService access) {
        this.products = products; this.shops = shops; this.access = access;
    }
    @GetMapping("/products")
    public String list(Model model) {
        model.addAttribute("products", products.list()); model.addAttribute("total", products.total());
        log.info("Пользователь {} открыл список покупок", access.currentUser().getUsername());
        return "list-products";
    }
    @GetMapping("/addProductForm")
    public String add(Model model) {
        access.requireWrite(); model.addAttribute("product", new ProductDto()); model.addAttribute("shops", shops.list());
        return "add-product-form";
    }
    @GetMapping("/showProductUpdateForm")
    public String edit(@RequestParam Long productId, Model model) {
        Product product = products.editable(productId);
        ProductDto dto = new ProductDto(); dto.setId(product.getId()); dto.setName(product.getName());
        dto.setPrice(product.getPrice()); dto.setQuantity(product.getQuantity()); dto.setShopId(product.getShop().getId());
        model.addAttribute("product", dto); model.addAttribute("shops", shops.list()); return "add-product-form";
    }
    @PostMapping("/saveProduct")
    public String save(@Valid @ModelAttribute("product") ProductDto dto, BindingResult result,
                       Model model, RedirectAttributes flash) {
        access.requireWrite();
        if (dto.getId() != null) products.editable(dto.getId());
        if (result.hasErrors()) { model.addAttribute("shops", shops.list()); return "add-product-form"; }
        products.save(dto); flash.addFlashAttribute("message", "Покупка сохранена"); return "redirect:/products";
    }
    @PostMapping("/deleteProduct")
    public String delete(@RequestParam Long productId, RedirectAttributes flash) {
        products.delete(productId); flash.addFlashAttribute("message", "Покупка удалена"); return "redirect:/products";
    }
}
