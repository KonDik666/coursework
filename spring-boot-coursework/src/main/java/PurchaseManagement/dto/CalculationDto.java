package PurchaseManagement.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
public class CalculationDto {
    @NotNull(message = "Введите бюджет")
    @DecimalMin(value = "0.01", message = "Бюджет должен быть больше нуля")
    @Digits(integer = 12, fraction = 2, message = "Бюджет: до 12 цифр и 2 знаков после запятой")
    private BigDecimal budget;
}
