package org.example.medicalappointment.logic;

import org.example.medicalappointment.data.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service("service")
public class ServiceDoctor {
    @Autowired
    private DoctorRepository doctorRepository;

    public Iterable<Medico> medicosFindAll() {
        return doctorRepository.findAll();
    }
}