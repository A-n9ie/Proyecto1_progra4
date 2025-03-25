package org.example.medicalappointment.logic;

import org.example.medicalappointment.data.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service("serviceAppointment")
public class ServiceAppointment {
    @Autowired
    private CitaRepository citaRepository;

    public Iterable<Cita> citasFindAll() {
        return citaRepository.findAll();
    }

    public void saveAppointment(Cita cita) {
        citaRepository.save(cita);
    }
}