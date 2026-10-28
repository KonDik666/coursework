package PurchaseManagement.service;

import PurchaseManagement.dto.UserDto;
import PurchaseManagement.entity.Role;
import PurchaseManagement.entity.User;
import PurchaseManagement.repository.RoleRepository;
import PurchaseManagement.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder encoder;
    private final AccessService access;
    public UserServiceImpl(UserRepository users, RoleRepository roles, PasswordEncoder encoder, AccessService access) {
        this.users = users; this.roles = roles; this.encoder = encoder; this.access = access;
    }

    @Override
    @Transactional
    public void saveUser(UserDto dto) {
        if (users.findByUsername(dto.getUsername()) != null)
            throw new IllegalArgumentException("Этот логин уже занят");
        User user = new User();
        user.setName(dto.getFirstName().trim() + " " + dto.getLastName().trim());
        user.setUsername(dto.getUsername());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.getRoles().add(roles.findByName("ROLE_READ_ONLY"));
        users.save(user);
        log.info("Регистрация пользователя {} с ролью READ_ONLY", user.getUsername());
    }
    @Override
    public User findByUsername(String username) { return users.findByUsername(username); }
    @Override
    public List<User> findAllUsers() { access.requireAdmin(); return users.findAll(); }

    @Override
    @Transactional
    public void addRole(Long userId, String roleName) {
        access.requireAdmin();
        if (!List.of("USER", "ADMIN").contains(roleName))
            throw new IllegalArgumentException("Можно добавить только USER или ADMIN");
        User user = users.findById(userId).orElseThrow(() -> new NoSuchElementException("Пользователь не найден"));
        Role role = roles.findByName("ROLE_" + roleName);
        if (user.getRoles().stream().noneMatch(value -> value.getId().equals(role.getId()))) {
            user.getRoles().add(role);
            users.save(user);
        }
        log.info("Пользователь {} добавил роль {} пользователю {}", access.currentUser().getUsername(), roleName, user.getUsername());
    }
}
