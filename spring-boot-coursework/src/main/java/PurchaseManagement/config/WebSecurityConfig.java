package PurchaseManagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public static PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/index", "/about", "/register/**", "/error", "/css/**", "/images/**").permitAll()
                .antMatchers("/users", "/users/**", "/actuator/**").hasRole("ADMIN")
                .antMatchers("/addShopForm", "/showShopUpdateForm", "/saveShop", "/deleteShop",
                        "/addProductForm", "/showProductUpdateForm", "/saveProduct", "/deleteProduct")
                    .hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
                .and().formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login")
                    .defaultSuccessUrl("/products", true).permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll());
        return http.build();
    }
}
