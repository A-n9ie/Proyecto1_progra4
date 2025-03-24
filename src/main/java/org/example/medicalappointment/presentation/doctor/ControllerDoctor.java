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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;


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

    //    Horario extendido
    @GetMapping("Schedule/{id}")
    public String showOne(@PathVariable Integer id, Model model) {
        model.addAttribute("medico", serviceDoctor.findDoctorById(id));
        model.addAttribute("medicoHorarios", serviceDoctor.obtenerMedicosConHorarios2());
        return "/presentation/patient/schedule";
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


    //    Horario extendido
    @GetMapping("/presentation/patient/schedule/{id}")
    public String showSchedule(@PathVariable Integer id, Model model) {
        // Encuentra el médico por ID
        Medico medico = serviceDoctor.findDoctorById(id);

        if (medico == null) {
            // Si el médico no se encuentra, redirige o muestra un error
            return "redirect:/error";
        }

        // Llamar al método para obtener los horarios del médico específico
        Map<Integer, List<String>> horarios = serviceDoctor.obtenerHorariosDeMedicoEspecifico(id);

        // Agregar el médico y sus días de horarios al modelo
        model.addAttribute("medico", medico);
        model.addAttribute("medicoHorarios", horarios);

        // Devolver la vista de Schedule
        return "/presentation/patient/schedule";
    }


}
