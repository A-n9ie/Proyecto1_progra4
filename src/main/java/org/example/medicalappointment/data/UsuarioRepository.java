package org.example.medicalappointment.data;

import org.example.medicalappointment.logic.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {
    Usuario findTopByOrderByIdDesc();

    Usuario findByUsuario(String username);
}

