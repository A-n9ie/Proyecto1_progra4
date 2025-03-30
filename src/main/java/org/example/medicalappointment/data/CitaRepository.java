package org.example.medicalappointment.data;

import org.apache.catalina.startup.ContextRuleSet;
import org.example.medicalappointment.logic.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CitaRepository extends JpaRepository<Cita, Integer> {
}
