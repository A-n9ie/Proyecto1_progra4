package org.example.medicalappointment.presentation.doctor;

import org.example.medicalappointment.logic.Medico;
import org.example.medicalappointment.logic.ServiceDoctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller("medicos")
public class ControllerDoctor {
    @Autowired
    private ServiceDoctor serviceDoctor;

    @GetMapping("/presentation/medicos/show")
    public String show(Model model) {
        model.addAttribute("medicos", serviceDoctor.medicosFindAll());
        return "presentation/";
    }
}
