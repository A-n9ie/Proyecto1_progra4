package org.example.medicalappointment.logic;

import org.example.medicalappointment.data.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service("servicePatient")
public class ServicePatient {
    @Autowired
    private PatientRepository patientRepository;

    public Iterable<Paciente> pacientesFindAll() {
        return patientRepository.findAll();
    }

    public void addPatient(Paciente paciente) {
        if(findPatient(paciente.getCedula()) != null) {
            throw new IllegalArgumentException("Patient already exists");
        }
        patientRepository.save(paciente);
    }

    public Paciente findPatient(String cedula) {return patientRepository.findByCedula(cedula);}

    public Paciente getPatientByUser(Usuario user){return patientRepository.findByUsuario(user);}

}