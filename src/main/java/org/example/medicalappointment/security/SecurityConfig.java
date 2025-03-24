package org.example.medicalappointment.security;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //ALL
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers("/","/presentation/patient/schedule/",
                        "/css/**","/images/**").permitAll()
                        //ADMIN
                        .requestMatchers("/presentation/usuarios/management/").hasAuthority("Administrador")
                        .anyRequest().authenticated()
                        //LOGIN
                ).formLogin(customizer -> customizer
                        .loginPage("/presentation/usuarios/login").failureUrl("/presentation/usuarios/login?error")
                        .permitAll()
                //LOGOUT
                ).logout(customizer -> customizer
                        .permitAll()
                        .logoutSuccessUrl("/")
                //notAuthorized
                ).exceptionHandling(customizer -> customizer
                        .accessDeniedPage("/notAuthorized")
                ).csrf(customizer -> customizer.disable());
        return http.build();
    }
}
