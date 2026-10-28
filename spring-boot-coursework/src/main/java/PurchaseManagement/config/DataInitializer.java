package PurchaseManagement.config;

import PurchaseManagement.entity.Role;
import PurchaseManagement.entity.User;
import PurchaseManagement.repository.RoleRepository;
import PurchaseManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initialize(RoleRepository roles, UserRepository users, PasswordEncoder encoder,
            @Value("${app.admin.username}") String username, @Value("${app.admin.password}") String password,
            @Value("${app.admin.enabled:true}") boolean enabled) {
        return args -> {
            for (String name : new String[]{"ROLE_READ_ONLY", "ROLE_USER", "ROLE_ADMIN"}) {
                if (roles.findByName(name) == null) {
                    Role role = new Role(); role.setName(name); roles.save(role);
                }
            }
            if (enabled && users.findByUsername(username) == null) {
                User user = new User();
                user.setName("Администратор"); user.setUsername(username);
                user.setPassword(encoder.encode(password));
                user.getRoles().add(roles.findByName("ROLE_ADMIN"));
                users.save(user);
            }
        };
    }
}
