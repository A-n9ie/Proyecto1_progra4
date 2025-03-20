package org.example.medicalappointment.data;


import org.example.medicalappointment.logic.Paciente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends CrudRepository<Paciente, String> {

    public Paciente findByCedula(String cedula);
}
