package org.example.medicalappointment.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }*/

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Nueva forma de deshabilitar CSRF en Spring Security 6.1+
                .authorizeHttpRequests(customizer -> customizer
//                        .requestMatchers("/**").permitAll()
                                .requestMatchers("/","/css/**", "/images/**","/presentation/usuarios/**","/presentation/patient/schedule/{id}").permitAll()
                                .requestMatchers("/presentation/patient/**").hasAuthority("Paciente")
                                .requestMatchers("/presentation/doctor/**").hasAuthority("Medico")
                                .requestMatchers("/presentation/administrador/management","/presentation/administrador/**").hasAuthority("Administrador")

                                .anyRequest().authenticated()
                )
                .formLogin(custumizer -> custumizer
                                .loginPage("/presentation/usuarios/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/", true)
                                .failureUrl("/presentation/usuarios/login?error=true")
                                .permitAll()
                                .successHandler((request, response, authentication) -> {
                                    // Verificar roles
                                    boolean isAdmin = authentication.getAuthorities().stream()
                                            .anyMatch(authority -> authority.getAuthority().equals("Administrador"));
                            boolean isPaciente = authentication.getAuthorities().stream()
                                    .anyMatch(authority -> authority.getAuthority().equals("Paciente"));
                            boolean isMedico = authentication.getAuthorities().stream()
                                    .anyMatch(authority -> authority.getAuthority().equals("Medico"));

                                    // Redirigir según el rol
                                    if (isAdmin) {
                                        response.sendRedirect("/presentation/administrador/management");
                            } else if (isPaciente) {
                                response.sendRedirect("/"); // O la URL que desees para paciente
                            } else if (isMedico) {
                                response.sendRedirect("/presentation/perfil/show"); // O la URL que desees para médico
                            } else {
                                response.sendRedirect("/"); // Redirigir a la página principal por defecto
                                    }
                                })
                )
                .logout(custumizer -> custumizer
                        .permitAll()
                        .logoutSuccessUrl("/")

                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/notAuthorized")
                );;

        return http.build();
    }

}
