package org.example.medicalappointment.presentation.patient;

import org.example.medicalappointment.logic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.*;
import jakarta.validation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@org.springframework.stereotype.Controller("pacientes")

public class ControllerPatient {
    @Autowired
    private ServicePatient servicePatient;
    @Autowired
    private ServiceAppointment serviceAppointment;

    @GetMapping("/presentation/pacientes/show")
    public String show(Model model) {
        model.addAttribute("usuarios", servicePatient.pacientesFindAll());
        return "presentation/usuarios/register";
    }

    @GetMapping("/presentation/patient/profile")
    public String profile(@ModelAttribute("usuario") Usuario user, Model model) {
        Paciente patient = servicePatient.getPatientByUser(user);
        model.addAttribute("persona", patient);
        return "presentation/usuarios/profile";
    }

    @PostMapping("/presentation/patient/book/save")
    public String saveAppointment(@RequestParam String fecha_cita, @RequestParam String hora_cita,
                                  @RequestParam Integer medicoId, @ModelAttribute("usuario") Usuario usuario, Model model) {

        LocalDate fecha = LocalDate.parse(fecha_cita, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Convertir hora_cita de String a LocalTime
        LocalTime hora = LocalTime.parse(hora_cita, DateTimeFormatter.ofPattern("HH:mm"));

        Cita nuevaCita = new Cita();
        nuevaCita.setFechaCita(fecha);
        nuevaCita.setHoraCita(hora);

        Paciente paciente = (Paciente) usuario.getPacientes();
        nuevaCita.setPaciente(paciente);

        if (paciente == null) {
            System.out.println("El paciente no está asignado correctamente.");
        } else {
            System.out.println("Paciente: " + paciente.getNombre());
        }

        serviceAppointment.saveAppointment(nuevaCita);

        model.addAttribute("mensaje", "Cita agendada exitosamente para el día " + fecha_cita + " a las " + hora_cita);

        return "presentation/patient/book/";
    }

}
