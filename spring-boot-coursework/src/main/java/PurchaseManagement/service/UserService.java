package PurchaseManagement.service;

import PurchaseManagement.dto.UserDto;
import PurchaseManagement.entity.User;
import java.util.List;

public interface UserService {
    void saveUser(UserDto dto);
    User findByUsername(String username);
    List<User> findAllUsers();
    void addRole(Long userId, String role);
}
