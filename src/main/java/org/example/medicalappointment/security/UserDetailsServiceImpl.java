package org.example.medicalappointment.security;

import org.example.medicalappointment.data.UsuarioRepository;
import org.example.medicalappointment.logic.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;
import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;
import org.example.medicalappointment.logic.Medico;
import org.example.medicalappointment.data.DoctorRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    DoctorRepository doctorRepository;

    /*
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
 */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Cargando usuario: " + username);
        Usuario user = usuarioRepository.findByUsuario(username);
        Medico medico = doctorRepository.findByUsuario(user);
        System.out.println(medico.getAprobado());

        try {
            if (user == null) {
                System.out.println("Usuario no encontrado: " + username);
                throw new UsernameNotFoundException("Username " + username + " not found");
            }

            if (!medico.getAprobado()) {
                throw new UsernameNotFoundException("Su usuario no está aprobado, comuníquese con el administrador");
            }
            return new UserDetailsImp(user);

        } catch (Exception e) {
            System.out.println("Error al cargar usuario: " + username);
            throw new UsernameNotFoundException("Username " + username + " not found");
        }
    }


}

