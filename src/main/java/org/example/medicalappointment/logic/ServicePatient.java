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

    public void addPatient(Usuario user, String id, String name) {
        Paciente paciente = new Paciente();
        paciente.setUsuario(user);
        paciente.setCedula(id);
        paciente.setNombre(name);
        patientRepository.save(paciente);
    }
}