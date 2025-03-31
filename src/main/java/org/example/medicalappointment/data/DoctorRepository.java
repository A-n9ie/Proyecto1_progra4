package org.example.medicalappointment.data;

import org.example.medicalappointment.logic.Medico;
import org.example.medicalappointment.logic.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface DoctorRepository extends CrudRepository<Medico, Integer> {
    public Medico findByCedula(String cedula);
    public Medico findByUsuario(Usuario usuario);

}
