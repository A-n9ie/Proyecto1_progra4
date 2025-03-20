package org.example.medicalappointment.presentation.doctor;

import org.example.medicalappointment.logic.ServiceDoctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.stereotype.Controller("medicos")
//@RequestMapping("/")
public class ControllerDoctor {
    @Autowired
    private ServiceDoctor serviceDoctor;

    @GetMapping("/")
    public String show(Model model) {
        model.addAttribute("medicos", serviceDoctor.medicosFindAll());
        return "presentation/principal/index";
    }
}
