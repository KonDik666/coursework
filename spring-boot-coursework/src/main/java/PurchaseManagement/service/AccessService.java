package PurchaseManagement.service;

import PurchaseManagement.entity.User;
import PurchaseManagement.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AccessService {
    private final UserRepository users;
    public AccessService(UserRepository users) { this.users = users; }

    public User currentUser() {
        User user = users.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user == null) throw new AccessDeniedException("Необходимо войти в приложение");
        return user;
    }

    public boolean hasRole(User user, String role) {
        return user.getRoles().stream().anyMatch(value -> value.getName().equals("ROLE_" + role));
    }

    public boolean isAdmin() { return hasRole(currentUser(), "ADMIN"); }
    public boolean isWriter() {
        User user = currentUser();
        return hasRole(user, "ADMIN") || hasRole(user, "USER");
    }
    public boolean ownRecordsOnly() {
        User user = currentUser();
        return !hasRole(user, "ADMIN") && hasRole(user, "USER");
    }
    public void requireAdmin() {
        if (!isAdmin()) throw new AccessDeniedException("Доступ разрешён только администратору");
    }
    public void requireWrite() {
        if (!isWriter()) throw new AccessDeniedException("Роль READ_ONLY разрешает только просмотр");
    }
    public void requireVisible(User owner) {
        if (ownRecordsOnly() && !owner.getId().equals(currentUser().getId()))
            throw new AccessDeniedException("Нет доступа к записи другого пользователя");
    }
    public void requireEditable(User owner) {
        requireWrite();
        if (!isAdmin() && !owner.getId().equals(currentUser().getId()))
            throw new AccessDeniedException("Нет доступа к записи другого пользователя");
    }
}
