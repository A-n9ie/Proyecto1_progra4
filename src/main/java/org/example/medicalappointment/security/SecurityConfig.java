package org.example.medicalappointment.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
/*
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public UserDetailsServiceImpl userDetailsService() {
        return userDetailsService;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }*/
    @Bean
    public PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //ALL
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers("/**").permitAll()
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

 /*   @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder()); // ← Asegurar que usa el mismo encriptador
        return authenticationManagerBuilder.build();
    }*/

}
