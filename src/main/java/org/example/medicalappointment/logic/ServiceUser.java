package org.example.medicalappointment.logic;

import org.example.medicalappointment.data.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service("service")
public class ServiceUser {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Iterable<Usuario> usuariosFindAll() {
        return usuarioRepository.findAll();
    }
}
