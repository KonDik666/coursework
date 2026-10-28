package PurchaseManagement.repository;

import PurchaseManagement.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByOwnerId(Long ownerId);
    boolean existsByShopId(Long shopId);
}
