package org.example.medicalappointment.data;

import org.apache.catalina.startup.ContextRuleSet;
import org.example.medicalappointment.logic.Cita;
import org.example.medicalappointment.logic.Medico;
import org.example.medicalappointment.logic.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Integer> {

    List<Cita> findCitaByMedico(Medico medico);
    List<Cita> findCitaByPaciente(Paciente paciente);
    List<LocalTime> findLocalTimeByMedico(Medico medico);

}
