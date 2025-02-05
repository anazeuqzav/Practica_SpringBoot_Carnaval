package com.iesvdc.carnaval.security;
import com.iesvdc.carnaval.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/register", "/auth/index").permitAll()  // Permitir acceso sin autenticación
                        .anyRequest().authenticated()  // Requiere autenticación para cualquier otra URL
                )
                .formLogin(login -> login
                        .loginPage("/auth/login") // Página personalizada para el login
                        .defaultSuccessUrl("/auth/index", true) // Redirigir a la página principal tras un login exitoso
                        .permitAll()  // Permitir el acceso sin autenticación
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // URL para hacer logout
                        .logoutSuccessUrl("/")  // Redirigir a la página principal tras el logout
                        .permitAll()  // Permitir el acceso sin autenticación
                )
                .csrf(csrf -> csrf.disable());  // Desactivar CSRF por simplicidad, aunque es importante para la seguridad

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Usa BCrypt para encriptar contraseñas
    }
}