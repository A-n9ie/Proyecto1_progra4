package org.example.medicalappointment.data;


import jakarta.persistence.criteria.CriteriaBuilder;
import org.example.medicalappointment.logic.Paciente;
import org.example.medicalappointment.logic.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends CrudRepository<Paciente, Integer> {
    public Paciente findByCedula(String cedula);
    public Paciente findByUsuario(Usuario usuario);
}
