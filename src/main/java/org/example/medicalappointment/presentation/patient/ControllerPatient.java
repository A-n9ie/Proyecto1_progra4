package org.example.medicalappointment.presentation.patient;

import org.example.medicalappointment.logic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.*;
import jakarta.validation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@org.springframework.stereotype.Controller("pacientes")

public class ControllerPatient {
    @Autowired
    private ServicePatient servicePatient;
    @Autowired
    private ServiceAppointment serviceAppointment;
    @Autowired
    private ServiceDoctor serviceDoctor;
    @Autowired
    private ServiceUser serviceUser;

    @GetMapping("/presentation/pacientes/show")
    public String show(Model model) {
        model.addAttribute("usuarios", servicePatient.pacientesFindAll());
        return "presentation/usuarios/register";
    }

    @GetMapping("/presentation/patient/profile")
    public String profile(@ModelAttribute("usuario") Usuario user, Model model) {
        Paciente patient = servicePatient.getPatientByUser(user);
        model.addAttribute("paciente", patient);
        return "presentation/usuarios/profile";
    }

    @GetMapping("/presentation/patient/edit")
    public String edit(@ModelAttribute("usuario") Usuario user, @ModelAttribute("paciente") Paciente patient) {
        servicePatient.editPatient(user, patient);
        return "redirect:/presentation/perfil/show";
    }

    @GetMapping("/presentation/patient/history/show")
    public String historyShow(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = serviceUser.getUser(username);
        Paciente paciente = servicePatient.getPatientByUser(usuario);
        model.addAttribute("citas", serviceAppointment.citasPaciente(paciente));
        return "/presentation/patient/history";
    }

    @GetMapping("/presentation/patient/history/filter")
    public String historyEstado(
            @RequestParam(value = "status", required = false, defaultValue = "All") String status,
            @RequestParam(value = "doctor", required = false, defaultValue = "") String doctor,
            Model model) {
        if(status.equals("All") && doctor.isEmpty())
            return "redirect:/presentation/patient/history/show";

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = serviceUser.getUser(username);
        Paciente paciente = servicePatient.getPatientByUser(usuario);

        List<Cita> citasFiltradas = serviceAppointment.citasPacienteFiltradas(paciente, status, doctor);

        model.addAttribute("citas", citasFiltradas);
        return "/presentation/patient/history";
    }


    @PostMapping("/presentation/patient/book/save")
    public String saveAppointment(@RequestParam("dia") String fecha_cita,
                                  @RequestParam("hora") String hora_cita,
                                  @RequestParam Integer medicoId,
                                  Model model) {
        try {
            // Obtener usuario autenticado
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Usuario usuario = serviceUser.getUser(username);
            System.out.println("Usuario autenticado: " + username);
            // Convertir fecha y hora
            LocalDate fecha = LocalDate.parse(fecha_cita, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalTime hora = LocalTime.parse(hora_cita, DateTimeFormatter.ofPattern("HH:mm"));
            System.out.println("Fecha y hora recibidas: " + fecha + " " + hora);

            // Buscar el médico
            Medico medico = serviceDoctor.findDoctorById(medicoId);
            if (medico == null) {
                model.addAttribute("error", "Médico no encontrado.");
                return "/presentation/patient/book";
            }
            System.out.println("Médico encontrado: " + medico);

            // Obtener pacientes del usuario

            Set<Paciente> pacientes = usuario.getPacientes();

            if (pacientes == null || pacientes.isEmpty()) {
                model.addAttribute("error", "No hay pacientes asociados al usuario.");
                return "/presentation/principal/index";
            }
            System.out.println("Pacientes encontrados: " + pacientes.size());

            Paciente paciente = pacientes.iterator().next();

            // Crear la cita
            Cita nuevaCita = new Cita();
            nuevaCita.setFechaCita(fecha);
            nuevaCita.setHoraCita(hora);
            nuevaCita.setMedico(medico);
            nuevaCita.setPaciente(paciente);

            // Guardar la cita
            serviceAppointment.saveAppointment(nuevaCita);
            System.out.println("Cita guardada: " + nuevaCita);
            // Mensaje de éxito
            model.addAttribute("mensaje", "Cita agendada exitosamente para el día " + fecha_cita + " a las " + hora_cita);

        } catch (DateTimeParseException e) {
            model.addAttribute("error", "Formato de fecha u hora incorrecto.");
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error al guardar la cita.");
        }

        return "redirect:/presentation/patient/book";
    }

    @GetMapping("/presentation/patient/book")
    public String book(Model model) {
        List<Cita> citas = serviceAppointment.citasFindAll();
        model.addAttribute("citas" , citas);
        return "presentation/patient/book";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "speciality", required = false) String speciality,
                         @RequestParam(value = "city", required = false) String city, Model model) {

        Iterable<Medico> doctorByLocation = serviceDoctor.obtenerMedicosPorLugarYEspecialidad(speciality, city);
        model.addAttribute("medicos", doctorByLocation);
        return "/presentation/principal/index";
    }



}
