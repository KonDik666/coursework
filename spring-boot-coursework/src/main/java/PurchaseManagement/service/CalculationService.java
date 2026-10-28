package PurchaseManagement.service;

import PurchaseManagement.dto.CalculationDto;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculationService {
    public BigDecimal calculate(CalculationDto dto, BigDecimal total) {
        return dto.getBudget().subtract(total).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal usedPercent(CalculationDto dto, BigDecimal total) {
        return total.multiply(BigDecimal.valueOf(100))
                .divide(dto.getBudget(), 2, RoundingMode.HALF_UP);
    }
}
