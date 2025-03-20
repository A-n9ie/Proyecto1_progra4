package org.example.medicalappointment.presentation.patient;

import org.example.medicalappointment.logic.Paciente;
import org.example.medicalappointment.logic.ServicePatient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.stereotype.Controller("pacientes")

public class ControllerPatient {
    @Autowired
    private ServicePatient servicePatient;

    @GetMapping("/presentation/usuarios/show")
    public String show(Model model) {
        model.addAttribute("usuarios", servicePatient.pacientesFindAll());
        return "presentation/usuarios/register";
    }

    @GetMapping("/presentation/usuarios/registerSys")
    public String register(Model model) {
        model.addAttribute("usuario", new Paciente());
        return "presentation/usuarios/register";
    }

    @PostMapping("/presentation/usuarios/patientRegister")
    public String patientRegister(@ModelAttribute Paciente paciente) {
        servicePatient.addPatient(paciente.getUsuario(), paciente.getCedula(), paciente.getNombre());
        return "redirect:/presentation/usuarios/registerSys";
    }


}
