package org.example.medicalappointment.data;


import org.example.medicalappointment.logic.Paciente;
import org.example.medicalappointment.logic.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends CrudRepository<Paciente, String> {
    public Paciente findByCedula(String cedula);
    public Paciente findByUsuario(Usuario usuario);
}
