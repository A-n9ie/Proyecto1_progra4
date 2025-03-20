package org.example.medicalappointment.presentation.doctor;

import org.example.medicalappointment.logic.Medico;
import org.example.medicalappointment.logic.Paciente;
import org.example.medicalappointment.logic.ServiceDoctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/presentation/medico/doctorRegister")
    public String doctorRegister(@ModelAttribute("doctor") Medico doctor) {

        if(serviceDoctor.findMedico(doctor.getCedula()) != null) {

        }

        if (doctor.getUsuario() == null || doctor.getCedula() == null || doctor.getNombre() == null) {
            return "errorPage";
        }

        serviceDoctor.addDoctor(doctor.getUsuario(), doctor.getCedula(), doctor.getNombre());
        return "redirect:/presentation/usuarios/registerSys";
    }


}
