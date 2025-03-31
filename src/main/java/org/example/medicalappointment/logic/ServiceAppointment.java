package org.example.medicalappointment.logic;

import org.example.medicalappointment.data.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
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

    public List<Cita> citasPacienteFiltradas(Paciente paciente, String estado, String doctor) {
        List<Cita> citasPaciente = citasPaciente(paciente);
        List<Cita> filtrada = new ArrayList<>();
        if ("All".equalsIgnoreCase(estado) && !doctor.isEmpty()) {
            for(Cita c: citasPaciente)
                if(c.getMedico().getNombre().toLowerCase().contains(doctor.toLowerCase()))
                    filtrada.add(c);
            return filtrada;
        }
        if (doctor.isEmpty() && !estado.isEmpty()){
            for(Cita c: citasPaciente)
                if(c.getEstado().toLowerCase().contains(estado.toLowerCase()))
                    filtrada.add(c);
            return filtrada;
        }
        for(Cita c: citasPaciente)
            if(c.getMedico().getNombre().toLowerCase().contains(doctor.toLowerCase()) &&
                    c.getEstado().toLowerCase().contains(estado.toLowerCase()))
                filtrada.add(c);
        return filtrada;
    }

}