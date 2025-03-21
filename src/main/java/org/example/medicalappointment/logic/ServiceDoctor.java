package org.example.medicalappointment.logic;

import org.example.medicalappointment.data.DoctorRepository;
import org.example.medicalappointment.data.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service("serviceDoctor")
public class ServiceDoctor {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private HorarioRepository horarioRepository;

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

    public List<HorariosMedico> diasAtencion(int id){
        return horarioRepository.findDiasByMedicoId(id);
    }

    public void agregarHorario(HorariosMedico horariosMedico) {
        horarioRepository.save(horariosMedico);
    }

}