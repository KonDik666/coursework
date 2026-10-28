package PurchaseManagement.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationLogging {
    @EventListener
    public void login(AuthenticationSuccessEvent event) { log.info("Вход пользователя {}", event.getAuthentication().getName()); }
    @EventListener
    public void failed(AbstractAuthenticationFailureEvent event) { log.warn("Неуспешная попытка входа"); }
    @EventListener
    public void logout(LogoutSuccessEvent event) { log.info("Выход пользователя {}", event.getAuthentication().getName()); }
}
