package org.example.medicalappointment.logic;

import org.example.medicalappointment.data.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service("serviceDoctor")
public class ServiceDoctor {
    @Autowired
    private DoctorRepository doctorRepository;

    public Iterable<Medico> medicosFindAll() {
        return doctorRepository.findAll();
    }

    public void addDoctor(Usuario user, String id, String name) {
        Medico doctor = new Medico();
        doctor.setUsuario(user);
        doctor.setCedula(id);
        doctor.setNombre(name);
        doctorRepository.save(doctor);
    }

    public Medico findMedico(String cedula) {
        return doctorRepository.findByCedula(cedula);
    }

}