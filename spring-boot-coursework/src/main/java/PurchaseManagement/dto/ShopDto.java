package PurchaseManagement.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.*;

@Getter
@Setter
public class ShopDto {
    private Long id;
    @NotBlank(message = "Введите название магазина")
    @Size(max = 100, message = "Не более 100 символов")
    private String name;
    @NotBlank(message = "Введите адрес")
    @Size(max = 200, message = "Не более 200 символов")
    private String address;
}
