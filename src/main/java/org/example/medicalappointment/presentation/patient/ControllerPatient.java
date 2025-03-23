package org.example.medicalappointment.presentation.patient;

import org.example.medicalappointment.logic.Paciente;
import org.example.medicalappointment.logic.ServicePatient;
import org.example.medicalappointment.logic.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.*;
import jakarta.validation.*;

@org.springframework.stereotype.Controller("pacientes")

public class ControllerPatient {
    @Autowired
    private ServicePatient servicePatient;

    @GetMapping("/presentation/pacientes/show")
    public String show(Model model) {
        model.addAttribute("usuarios", servicePatient.pacientesFindAll());
        return "presentation/usuarios/register";
    }

    @GetMapping("/presentation/usuarios/registerSystem")
    public String register(Model model) {
        model.addAttribute("usuario", new Paciente());
        return "presentation/usuarios/register";
    }

    @GetMapping("/presentation/patient/create")
    public String create(@Valid @ModelAttribute("paciente") Paciente patient, BindingResult result) {
        if (patient.getUsuario() == null || patient.getCedula() == null || patient.getNombre() == null) {
            return "errorPage";
        }
        try {
            servicePatient.addPatient(patient.getUsuario(), patient);
            return "redirect:/presentation/usuarios/registerSys";
        } catch (Exception e) {
            result.addError(new FieldError("paciente", "cedula", e.getMessage()));
            return "redirect:/presentation/usuarios/registerSys";
        }
    }

    @GetMapping("/presentation/patient/profile")
    public String profile(@ModelAttribute("usuario") Usuario user, Model model) {
        Paciente patient = servicePatient.getPatientByUser(user);
        model.addAttribute("persona", patient);
        return "presentation/usuarios/profile";
    }
}
