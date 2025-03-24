package org.example.medicalappointment.logic;

import org.example.medicalappointment.data.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service("serviceUser")
public class ServiceUser {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Iterable<Usuario> usuariosFindAll() {
        return usuarioRepository.findAll();
    }

    public Usuario getLastUser() {
        return usuarioRepository.findTopByOrderByIdDesc();
    }

    public Usuario getUser(String username) {
        return usuarioRepository.findByUsuario(username);
    }

    public void addUser(Usuario user, String password) {
        if (getUser(user.getUsername()) != null) {
            throw new IllegalArgumentException("User already exists");
        }
        if(!user.getClave().equals(password)) {
            throw new IllegalArgumentException("Wrong password");
        }
        usuarioRepository.save(user);
    }
}
