package org.example.medicalappointment.security;

import org.example.medicalappointment.data.UsuarioRepository;
import org.example.medicalappointment.logic.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;


import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            Usuario user = usuarioRepository.findByUsuario(username);
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getClave())
                    .roles()
                    .build();
        } catch (Exception e) {
            throw new UsernameNotFoundException("Username " + username + " not found");
        }
    }
}
