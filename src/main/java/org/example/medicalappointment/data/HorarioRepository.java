package org.example.medicalappointment.data;

import org.example.medicalappointment.logic.HorariosMedico;
import org.example.medicalappointment.logic.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HorarioRepository extends CrudRepository<HorariosMedico, Integer> {

    List<HorariosMedico> findByMedicoId(Integer idMedico);
    void deleteAllByMedico(Medico medico);
}


