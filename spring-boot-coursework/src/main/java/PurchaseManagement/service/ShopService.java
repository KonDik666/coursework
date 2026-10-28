package PurchaseManagement.service;

import PurchaseManagement.dto.ShopDto;
import PurchaseManagement.entity.Shop;
import PurchaseManagement.repository.ShopRepository;
import PurchaseManagement.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class ShopService {
    private final ShopRepository shops;
    private final ProductRepository products;
    private final AccessService access;
    public ShopService(ShopRepository shops, ProductRepository products, AccessService access) {
        this.shops = shops; this.products = products; this.access = access;
    }
    public List<Shop> list() {
        return access.ownRecordsOnly() ? shops.findByOwnerId(access.currentUser().getId()) : shops.findAll();
    }
    public Shop get(Long id) {
        Shop shop = shops.findById(id).orElseThrow(() -> new NoSuchElementException("Магазин не найден"));
        access.requireVisible(shop.getOwner());
        return shop;
    }
    public Shop editable(Long id) { Shop shop = get(id); access.requireEditable(shop.getOwner()); return shop; }
    @Transactional
    public void save(ShopDto dto) {
        access.requireWrite();
        Shop shop = dto.getId() == null ? new Shop() : editable(dto.getId());
        if (dto.getId() == null) shop.setOwner(access.currentUser());
        shop.setName(dto.getName().trim());
        shop.setAddress(dto.getAddress().trim());
        shops.save(shop);
        log.info("Пользователь {} сохранил магазин id={}", access.currentUser().getUsername(), shop.getId());
    }
    @Transactional
    public void delete(Long id) {
        Shop shop = editable(id);
        if (products.existsByShopId(id))
            throw new IllegalArgumentException("Сначала удалите покупки этого магазина или перенесите их в другой магазин");
        shops.delete(shop);
        log.info("Пользователь {} удалил магазин id={}", access.currentUser().getUsername(), id);
    }
}
