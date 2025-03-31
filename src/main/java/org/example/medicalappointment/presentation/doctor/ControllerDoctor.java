package org.example.medicalappointment.presentation.doctor;

import org.example.medicalappointment.data.HorarioRepository;
import org.example.medicalappointment.logic.HorariosMedico;
import org.example.medicalappointment.logic.Medico;
import org.example.medicalappointment.logic.ServiceDoctor;
import org.example.medicalappointment.logic.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.*;
import jakarta.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@org.springframework.stereotype.Controller("medicos")

public class ControllerDoctor {
    @Autowired
    private ServiceDoctor serviceDoctor;
    @Autowired
    private HorarioRepository horarioRepository;


    @GetMapping()
    public String show(Model model) {
        model.addAttribute("medicos", serviceDoctor.medicosFindAll());
        model.addAttribute("medicosHorarios", serviceDoctor.obtenerMedicosConHorarios());
        return "/presentation/principal/index";
    }

    @GetMapping("/presentation/doctor/profile")
    public String profile(@ModelAttribute("usuario") Usuario user, Model model) {
        Medico doctor = serviceDoctor.getDoctorbyUser(user);

        List<String> horarios =
                horarioRepository.findByMedicoId(doctor.getId()).stream()
                .map(HorariosMedico::getDia)
                .collect(Collectors.toList());
        String[] days = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};

        model.addAttribute("days", days);
        model.addAttribute("medico", doctor);
        model.addAttribute("selectedDays", horarios);
        return "presentation/usuarios/profile";
    }

    @GetMapping("/presentation/doctor/edit")
    public String edit(@ModelAttribute("usuario") Usuario user,
                       @ModelAttribute("medico") Medico doctor,
                       @ModelAttribute("days") List<String> selectedDays) {
        serviceDoctor.editDoctor(user, doctor);
        serviceDoctor.editDays(doctor, selectedDays);
        return "redirect:/presentation/perfil/show";
    }

    @GetMapping("/presentation/patient/schedule/{id}")
    public String showSchedule(@PathVariable Integer id, @RequestParam(defaultValue = "0") int page, Model model) {
        Medico medico = serviceDoctor.findDoctorById(id);
        if (medico == null) {
            return "redirect:/error";
        }

        Map<Integer, List<String>> horarios = serviceDoctor.obtenerHorariosDeMedicoEspecifico(id);

        List<String> fechas = horarios.get(medico.getId());
        if (fechas == null) {
            return "redirect:/error";
        }

        List<String> diasApartirDelTercerDia = fechas.stream()
                .skip(2) // Saltamos los dos primeros días
                .collect(Collectors.toList());

        int pageSize = 3;
        int totalDias = fechas.size();

        List<String> diasPaginados = fechas.stream()
                .skip(page * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());

        model.addAttribute("medico", medico);
        model.addAttribute("medicoHorarios", diasPaginados);
        model.addAttribute("page", page);
        model.addAttribute("totalDias", totalDias);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("medicosHorarios", horarios);

        return "/presentation/patient/schedule";
    }

    @GetMapping("/presentation/administrador/management")
    public String showDocsForApproval(Model model) {
        model.addAttribute("Doctors", serviceDoctor.medicosFindAll());
        return "/presentation/administrator/management";
    }



}
