package PurchaseManagement.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.*;

@Getter
@Setter
public class UserDto {
    @NotBlank(message = "Введите имя")
    @Size(max = 80, message = "Не более 80 символов")
    private String firstName;
    @NotBlank(message = "Введите фамилию")
    @Size(max = 80, message = "Не более 80 символов")
    private String lastName;
    @NotBlank(message = "Введите логин")
    @Pattern(regexp = "[A-Za-z0-9_.-]{3,40}", message = "Логин: 3–40 латинских букв, цифр или символов _ . -")
    private String username;
    @NotBlank(message = "Введите пароль")
    @Size(min = 6, max = 60, message = "Пароль должен содержать от 6 до 60 символов")
    private String password;
}
