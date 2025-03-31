package org.example.medicalappointment.data;

import org.example.medicalappointment.logic.Medico;
import org.example.medicalappointment.logic.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface DoctorRepository extends CrudRepository<Medico, Integer> {
    public Medico findByCedula(String cedula);
    public Medico findByUsuario(Usuario usuario);
//    public Iterable<Medico> findMedicoByLugar(String lugar);

    // Buscar médicos por ciudad (búsqueda parcial)
    Iterable<Medico> findByLugarAtencionContainingIgnoreCase(String ciudad);

    // Buscar médicos por especialidad (búsqueda parcial)
    Iterable<Medico> findByEspecialidadContainingIgnoreCase(String especialidad);

    // Buscar médicos por especialidad y ciudad (búsqueda parcial)
    Iterable<Medico> findByEspecialidadContainingIgnoreCaseAndLugarAtencionContainingIgnoreCase(String especialidad, String ciudad);
}
