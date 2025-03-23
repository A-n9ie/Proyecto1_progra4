package org.example.medicalappointment.presentation.doctor;

import org.example.medicalappointment.data.HorarioRepository;
import org.example.medicalappointment.logic.Medico;
import org.example.medicalappointment.logic.ServiceDoctor;
import org.example.medicalappointment.logic.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.*;
import jakarta.validation.*;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping("/presentation/doctor/create")
    public String create(@Valid @ModelAttribute("doctor") Medico doctor, BindingResult result) {
        if (doctor.getUsuario() == null || doctor.getCedula() == null || doctor.getNombre() == null) {
            return "errorPage";
        }
        try {
            serviceDoctor.addDoctor(doctor.getUsuario(), doctor);
            return "redirect:/presentation/usuarios/registerSys";
        } catch (Exception e) {
            result.addError(new FieldError("doctor", "cedula", e.getMessage()));
            return "redirect:/presentation/usuarios/registerSys";
        }
    }

    @GetMapping("/presentation/doctor/profile")
    public String profile(@ModelAttribute("usuario") Usuario user, Model model) {
        Medico doctor = serviceDoctor.getDoctorbyUser(user);
        model.addAttribute("persona", doctor);
        return "presentation/usuarios/profile";
    }



}
