package org.example.medicalappointment.logic;

import org.example.medicalappointment.data.DoctorRepository;
import org.example.medicalappointment.data.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
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

    public Medico findDoctorById(Integer id) {
        return doctorRepository.findById(id).orElse(null);
    }

    public void addDoctor(Medico doctor) {
        doctorRepository.save(doctor);
    }

    public Medico findDoctor(String cedula) {
        return doctorRepository.findByCedula(cedula);
    }

    public Medico getDoctorbyUser(Usuario usuario) {return doctorRepository.findByUsuario(usuario);}

    public Iterable<HorariosMedico> horariosMedicosFindAll() {
        return horarioRepository.findAll();
    }

    public Map<Integer, List<String>> obtenerMedicosConHorarios() {
        //Lista de horarios
        List<HorariosMedico> horarios = (List<HorariosMedico>) horariosMedicosFindAll();
       //id, lista de horarios
        Map<Integer, List<String>> medicosConHorarios = new HashMap<>();

       //hoy
        LocalDate fechaBase = LocalDate.now();
        //dia, id
        Map<String, Integer> diasDeLaSemana = new HashMap<>();
        diasDeLaSemana.put("Lunes", 1);
        diasDeLaSemana.put("Martes", 2);
        diasDeLaSemana.put("Miercoles", 3);
        diasDeLaSemana.put("Jueves", 4);
        diasDeLaSemana.put("Viernes", 5);
        diasDeLaSemana.put("Sabado", 6);
        diasDeLaSemana.put("Domingo", 7);

        for (HorariosMedico horario : horarios) {
            //sacr el id del medico
            Integer medicoId = horario.getMedico().getId();
            String dia = horario.getDia();

            LocalDate fechaDia = calcularFechaParaDiaSemana(fechaBase, diasDeLaSemana.get(dia));

            // Agregar la fecha al mapa
            medicosConHorarios.putIfAbsent(medicoId, new ArrayList<>());
            List<String> diasDelMedico = medicosConHorarios.get(medicoId);

            if (!diasDelMedico.contains(fechaDia.toString())) {
                diasDelMedico.add(fechaDia.toString());  // Guardamos la fecha como un String (formato ISO)
            }
        }

        return medicosConHorarios;
    }

    private LocalDate calcularFechaParaDiaSemana(LocalDate fechaBase, int diaSemana) {
        //hoy
        LocalDate fecha = fechaBase;

        int diasHastaDia = diaSemana - fecha.getDayOfWeek().getValue();
        if (diasHastaDia < 0) {
            diasHastaDia += 7;
        }

        return fecha.plusDays(diasHastaDia);
    }

    public void agregarHorario(HorariosMedico horariosMedico) {
        horarioRepository.save(horariosMedico);
    }

    public Map<Integer, List<String>> obtenerHorariosDeMedicoEspecifico(Integer medicoId) {

        List<HorariosMedico> horarios = (List<HorariosMedico>) horariosMedicosFindAll();

        Map<Integer, List<String>> medicosConHorarios = new HashMap<>();

        LocalDate fechaBase = LocalDate.now();

        // Map de días de la semana
        Map<String, Integer> diasDeLaSemana = new HashMap<>();
        diasDeLaSemana.put("Lunes", 1);
        diasDeLaSemana.put("Martes", 2);
        diasDeLaSemana.put("Miercoles", 3);
        diasDeLaSemana.put("Jueves", 4);
        diasDeLaSemana.put("Viernes", 5);
        diasDeLaSemana.put("Sabado", 6);
        diasDeLaSemana.put("Domingo", 7);

        for (HorariosMedico horario : horarios) {
            if (horario.getMedico().getId().equals(medicoId)) {
                String dia = horario.getDia();

                LocalDate fechaDia = calcularFechaParaDiaSemana(fechaBase, diasDeLaSemana.get(dia));
                medicosConHorarios.putIfAbsent(medicoId, new ArrayList<>());

                // Obtenemos la lista de días para el médico
                List<String> fechasDelMedico = medicosConHorarios.get(medicoId);

                // Si el día aún no está en la lista, lo agregamos
                if (!fechasDelMedico.contains(fechaDia.toString())) {
                    fechasDelMedico.add(fechaDia.toString());
                }
            }
        }

        return medicosConHorarios;
    }

}