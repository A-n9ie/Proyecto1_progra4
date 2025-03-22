package org.example.medicalappointment.presentation.doctor;

import org.example.medicalappointment.data.HorarioRepository;
import org.example.medicalappointment.logic.HorariosMedico;
import org.example.medicalappointment.logic.Medico;
import org.example.medicalappointment.logic.MedicosConHorarios;
import org.example.medicalappointment.logic.ServiceDoctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@org.springframework.stereotype.Controller("medicos")

//@RequestMapping("/")
public class ControllerDoctor {
    @Autowired
    private ServiceDoctor serviceDoctor;
    @Autowired
    private HorarioRepository horarioRepository;


    @GetMapping("")
    public String show(Model model) {
        model.addAttribute("medicos", serviceDoctor.medicosFindAll());
        model.addAttribute("medicosHorarios", serviceDoctor.obtenerMedicosConHorarios2());
        return "/presentation/principal/index";
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
