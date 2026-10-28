package PurchaseManagement.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
public class ProductDto {
    private Long id;
    @NotBlank(message = "Введите название покупки")
    @Size(max = 100, message = "Не более 100 символов")
    private String name;
    @NotNull(message = "Введите цену")
    @DecimalMin(value = "0.01", message = "Цена должна быть больше нуля")
    @Digits(integer = 10, fraction = 2, message = "Цена: до 10 цифр и 2 знаков после запятой")
    private BigDecimal price;
    @NotNull(message = "Введите количество")
    @Min(value = 1, message = "Количество должно быть не меньше 1")
    @Max(value = 1000000, message = "Количество должно быть не больше 1000000")
    private Integer quantity;
    @NotNull(message = "Выберите магазин")
    private Long shopId;
}
