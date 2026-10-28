package PurchaseManagement.service;

import PurchaseManagement.dto.ProductDto;
import PurchaseManagement.entity.Product;
import PurchaseManagement.entity.Shop;
import PurchaseManagement.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class ProductService {
    private final ProductRepository products;
    private final ShopService shops;
    private final AccessService access;
    public ProductService(ProductRepository products, ShopService shops, AccessService access) {
        this.products = products; this.shops = shops; this.access = access;
    }
    public List<Product> list() {
        return access.ownRecordsOnly() ? products.findByOwnerId(access.currentUser().getId()) : products.findAll();
    }
    public Product get(Long id) {
        Product value = products.findById(id).orElseThrow(() -> new NoSuchElementException("Покупка не найдена"));
        access.requireVisible(value.getOwner());
        return value;
    }
    public Product editable(Long id) { Product value = get(id); access.requireEditable(value.getOwner()); return value; }
    public BigDecimal total() { return list().stream().map(Product::getCost).reduce(BigDecimal.ZERO, BigDecimal::add); }
    @Transactional
    public void save(ProductDto dto) {
        access.requireWrite();
        Product value = dto.getId() == null ? new Product() : editable(dto.getId());
        Shop shop = shops.get(dto.getShopId());
        access.requireEditable(shop.getOwner());
        if (dto.getId() == null) value.setOwner(access.currentUser());
        value.setName(dto.getName().trim());
        value.setPrice(dto.getPrice());
        value.setQuantity(dto.getQuantity());
        value.setShop(shop);
        products.save(value);
        log.info("Пользователь {} сохранил покупку id={}", access.currentUser().getUsername(), value.getId());
    }
    @Transactional
    public void delete(Long id) {
        products.delete(editable(id));
        log.info("Пользователь {} удалил покупку id={}", access.currentUser().getUsername(), id);
    }
}
