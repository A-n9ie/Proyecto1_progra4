package org.example.medicalappointment.logic;

import org.example.medicalappointment.data.DoctorRepository;
import org.example.medicalappointment.data.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@org.springframework.stereotype.Service("serviceDoctor")
public class ServiceDoctor {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private HorarioRepository horarioRepository;

    public Iterable<Medico> medicosFindAll() {
        return doctorRepository.findAll();
    }

    public void addDoctor(Usuario user, Medico doctor) {
        if(findDoctor(doctor.getCedula()) != null) {
            throw new IllegalArgumentException("Doctor already exists");
        }
        doctor.setUsuario(user);
        doctorRepository.save(doctor);
    }

    public Medico findDoctor(String cedula) {
        return doctorRepository.findByCedula(cedula);
    }


    public Iterable<HorariosMedico> horariosMedicosFindAll() {
        return horarioRepository.findAll();
    }

    public List<MedicosConHorarios> obtenerMedicosConHorarios1() {
        List<HorariosMedico> horarios = (List<HorariosMedico>) horariosMedicosFindAll();
        List<MedicosConHorarios> medicosConHorarios = new ArrayList<>();

        horarios.stream()
                .collect(Collectors.groupingBy(horario -> horario.getMedico().getId()))
                .forEach((medicoId, horariosMedico) -> {
                    MedicosConHorarios medicoConHorarios = new MedicosConHorarios(medicoId, horariosMedico);
                    medicosConHorarios.add(medicoConHorarios);
                });

        return medicosConHorarios;
    }

    public Map<Integer, List<String>> obtenerMedicosConHorarios2() {
        // Obtener todos los horarios médicos
        List<HorariosMedico> horarios = (List<HorariosMedico>) horariosMedicosFindAll();

        // Crear un mapa donde la clave es el ID del médico
        // y el valor es una lista de días de la semana
        Map<Integer, List<String>> medicosConHorarios = new HashMap<>();

        // Procesar cada horario
        for (HorariosMedico horario : horarios) {
            Integer medicoId = horario.getMedico().getId();
            String dia = horario.getDia(); // Asumimos que 'dia' es un string que representa el día de la semana

            // Si el médico aún no está en el mapa, lo agregamos con una lista vacía de días
            medicosConHorarios.putIfAbsent(medicoId, new ArrayList<>());

            // Obtenemos la lista de días para el médico
            List<String> diasDelMedico = medicosConHorarios.get(medicoId);

            // Si el día aún no está en la lista, lo agregamos
            if (!diasDelMedico.contains(dia)) {
                diasDelMedico.add(dia);
            }
        }

        return medicosConHorarios;
    }

    public void agregarHorario(HorariosMedico horariosMedico) {
        horarioRepository.save(horariosMedico);
    }



}