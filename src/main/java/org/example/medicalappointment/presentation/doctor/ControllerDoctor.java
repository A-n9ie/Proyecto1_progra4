package org.example.medicalappointment.presentation.doctor;

import org.example.medicalappointment.data.HorarioRepository;
import org.example.medicalappointment.logic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@org.springframework.stereotype.Controller("medicos")

public class ControllerDoctor {
    @Autowired
    private ServiceDoctor serviceDoctor;
    @Autowired
    private HorarioRepository horarioRepository;
    @Autowired
    private ServiceAppointment serviceAppointment;
    @Autowired
    private ServiceUser serviceUser;


    @GetMapping()
    public String show(Model model) {
        model.addAttribute("medicos", serviceDoctor.medicosFindAll());
        model.addAttribute("medicosHorarios", serviceDoctor.obtenerMedicosConHorarios());
        return "/presentation/principal/index";
    }

    @GetMapping("/presentation/doctor/profile")
    public String profile(@ModelAttribute("usuario") Usuario user, Model model) {
        Medico doctor = serviceDoctor.getDoctorbyUser(user);

        int numero = 0;
        numero = Integer.parseInt(doctor.getFrecuenciaCitas().split(" ")[0]);
        String frecuencia = "horas";

        try {
            String[] parts = doctor.getFrecuenciaCitas().split(" ");
            if (parts.length == 2) {
                numero = Integer.parseInt(parts[0]);
                frecuencia = parts[1];
            } else {
                numero = 1;
                frecuencia = "horas";
            }
        } catch (NumberFormatException e) {
            numero = 1;
            frecuencia = "horas";
        }
        model.addAttribute("numero", numero);
        model.addAttribute("frecuencia", frecuencia);

        List<String> horarios =
                horarioRepository.findByMedicoId(doctor.getId()).stream()
                .map(HorariosMedico::getDia)
                .collect(Collectors.toList());
        String[] days = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};

        model.addAttribute("days", days);
        model.addAttribute("medico", doctor);
        model.addAttribute("selectedDays", horarios);
        return "presentation/usuarios/profile";
    }

    @GetMapping("/presentation/doctor/edit")
    public String edit(@ModelAttribute("usuario") Usuario user,
                       @ModelAttribute("medico") Medico doctor,
                       @ModelAttribute("days") List<String> selectedDays,
                       @ModelAttribute("numero") Integer numero,
                       @ModelAttribute("frecuencia") String frecuencia,
                       Model model) {
        if (selectedDays == null || selectedDays.isEmpty()
                || numero < 1
                || doctor.getCostoConsulta().compareTo(BigDecimal.ZERO) <= 0) {
            model.addAttribute("error", "All fields must be filled out and contain positive values");
            String[] days = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
            List<String> horarios =
                    horarioRepository.findByMedicoId(doctor.getId()).stream()
                            .map(HorariosMedico::getDia)
                            .collect(Collectors.toList());
            model.addAttribute("numero", numero);
            model.addAttribute("frecuencia", frecuencia);
            model.addAttribute("days", days);
            model.addAttribute("selectedDays", horarios);
            return "/presentation/usuarios/profile";
        }
        doctor.setFrecuenciaCitas(numero + " " + frecuencia);
        serviceDoctor.editDoctor(user, doctor);
        serviceDoctor.editDays(doctor, selectedDays);
        return "redirect:/presentation/perfil/show";
    }


    @GetMapping("/presentation/doctor/appointment/show")
    public String historyShow(@ModelAttribute("usuario") Usuario user,
            Model model) {
        Medico medico = serviceDoctor.getDoctorbyUser(user);

        List<Cita> citas = serviceAppointment.citasMedico(medico);
        model.addAttribute("citas", citas);
        model.addAttribute("nombre", medico.getNombre());

        return "/presentation/doctor/appointment";
    }

    @GetMapping("/presentation/doctor/history/filter")
    public String historyEstado(
            @RequestParam(value = "status", required = false, defaultValue = "All") String status,
            @RequestParam(value = "patient", required = false, defaultValue = "") String paciente,
            @RequestParam(value = "show", required = false) Integer show,
            @RequestParam(value = "approve", required = false) Integer approve,
            @RequestParam(value = "cancel", required = false) Integer cancel,
            Model model) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario user = serviceUser.getUser(username);
        Medico medico = serviceDoctor.getDoctorbyUser(user);

        if (show != null) {
            Cita citaSeleccionada = serviceAppointment.getCitaById(show);
            model.addAttribute("citaSeleccionada", citaSeleccionada);
        }
        if (approve != null) {
            Cita citaSeleccionada = serviceAppointment.getCitaById(approve);
            if (citaSeleccionada != null) {
                serviceAppointment.saveAppointment(citaSeleccionada);
                model.addAttribute("citaAprovada", citaSeleccionada);
            }
        }
        if (cancel != null) {
            Cita citaSeleccionada = serviceAppointment.getCitaById(cancel);
            if (citaSeleccionada != null) {
                serviceAppointment.deleteAppointment(citaSeleccionada);
            }
        }

        List<Cita> citasFiltradas = serviceAppointment.citasMedicoFiltradas(medico, status, paciente);
        if(status.equals("All") && paciente.isEmpty()) {
            citasFiltradas = serviceAppointment.citasMedico(medico);
        }
        model.addAttribute("citas", citasFiltradas);
        model.addAttribute("nombre", medico.getNombre());


        return "/presentation/doctor/appointment";
    }

    @PostMapping("/presentation/doctor/history/saveNote")
    public String saveNote(
            @RequestParam("citaId") Integer citaId,
            @RequestParam("anotaciones") String anotaciones) {

        Cita cita = serviceAppointment.getCitaById(citaId);
        if (cita != null) {
            cita.setAnotaciones(anotaciones);
            cita.setEstado("Atendida");
            serviceAppointment.saveAppointment(cita);
        }

        return "redirect:/presentation/doctor/history/filter";
    }


    @GetMapping("/presentation/patient/schedule/{id}")
    public String showSchedule(@PathVariable Integer id, @RequestParam(defaultValue = "0") int page, Model model) {
        Medico medico = serviceDoctor.findDoctorById(id);
        if (medico == null) {
            return "redirect:/error";
        }

        int pageSize = 3;
        Map<Integer, List<String>> horarios = serviceDoctor.obtenerHorariosDeMedicoEspecifico(id);

        List<String> fechas = horarios.get(medico.getId());
        if (fechas == null) {
            return "redirect:/error";
        }

        int totalDias = fechas.size();
        int totalPages = (int) Math.ceil((double) totalDias / pageSize);


        model.addAttribute("medico", medico);
        model.addAttribute("medicoHorarios", fechas);
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

    @PostMapping("/aprobar")
    public String aprobarDoctor(@RequestParam("id") int id, Model model) {
        serviceDoctor.cambiarEstado(id, true);
        model.addAttribute("Doctors", serviceDoctor.medicosFindAll());
        return "/presentation/administrator/management";
    }


}
