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

    @Query("SELECT m FROM Medico m WHERE LOWER(m.lugarAtencion) LIKE LOWER(CONCAT('%', :ciudad, '%'))")
    Iterable<Medico> findByCiudad(@Param("ciudad") String ciudad);

    // Buscar médicos por especialidad (búsqueda parcial)
    @Query("SELECT m FROM Medico m WHERE LOWER(m.especialidad) LIKE LOWER(CONCAT('%', :especialidad, '%'))")
    Iterable<Medico> findByEspecialidad(@Param("especialidad") String especialidad);

    // Buscar médicos por especialidad y ciudad (búsqueda parcial)
    @Query("SELECT m FROM Medico m WHERE LOWER(m.especialidad) LIKE LOWER(CONCAT('%', :especialidad, '%')) AND LOWER(m.lugarAtencion) LIKE LOWER(CONCAT('%', :ciudad, '%'))")
    Iterable<Medico> findByEspecialidadAndCiudad(@Param("especialidad") String especialidad, @Param("ciudad") String ciudad);
}
