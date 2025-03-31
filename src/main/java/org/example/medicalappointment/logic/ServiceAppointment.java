package org.example.medicalappointment.logic;

import org.example.medicalappointment.data.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service("serviceAppointment")
public class ServiceAppointment {
    @Autowired
    private CitaRepository citaRepository;

    public List<Cita> citasFindAll() {
        return citaRepository.findAll();
    }

    public void saveAppointment(Cita cita) {
        citaRepository.save(cita);
    }

    public List<Cita> citasPaciente(Paciente paciente){return citaRepository.findCitaByPaciente(paciente);}
}