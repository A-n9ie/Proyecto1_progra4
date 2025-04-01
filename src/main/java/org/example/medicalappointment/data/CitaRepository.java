package org.example.medicalappointment.data;

import org.example.medicalappointment.logic.Cita;
import org.example.medicalappointment.logic.Medico;
import org.example.medicalappointment.logic.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Integer> {

    List<Cita> findCitaByMedico(Medico medico);
    List<Cita> findCitaByPacienteOrderByFechaCitaDescHoraCitaAsc(Paciente paciente);
    List<Cita> findCitaByMedicoOrderByFechaCitaDescHoraCitaAsc(Medico doctor);
    List<LocalTime> findLocalTimeByMedico(Medico medico);

}
