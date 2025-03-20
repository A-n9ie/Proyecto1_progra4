package org.example.medicalappointment.data;

import org.example.medicalappointment.logic.Medico;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends CrudRepository<Medico, Integer> {
}
