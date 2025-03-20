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

    @GetMapping("/presentation/patient/patientRegister")
    public String patientRegister(@ModelAttribute("paciente") Paciente patient) {
        if (patient.getUsuario() == null || patient.getCedula() == null || patient.getNombre() == null) {
            return "errorPage";
        }

        servicePatient.addPatient(patient.getUsuario(), patient.getCedula(), patient.getNombre());
        return "redirect:/presentation/usuarios/registerSys";
    }
}
