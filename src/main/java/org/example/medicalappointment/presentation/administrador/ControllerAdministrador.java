package org.example.medicalappointment.presentation.administrador;


import org.example.medicalappointment.data.HorarioRepository;
import org.example.medicalappointment.logic.Medico;
import org.example.medicalappointment.logic.ServiceAppointment;
import org.example.medicalappointment.logic.ServiceDoctor;
import org.example.medicalappointment.logic.ServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.*;
import jakarta.validation.*;



@org.springframework.stereotype.Controller("administrador")
public class ControllerAdministrador {
    @Autowired
    private ServiceDoctor serviceDoctor;

    @GetMapping("/admin/management")
    public String mostrarManagement() {
        return "presentation/administrador/management";
    }

    @GetMapping("/filtrarDocs")
    public String filtrarDoctores(@RequestParam(required = false) String speciality,
                                  @RequestParam(required = false) String city,
                                  Model model) {
        Iterable<Medico> doctoresFiltrados = serviceDoctor.obtenerMedicosPorLugarYEspecialidad(speciality, city);
        model.addAttribute("Doctors", doctoresFiltrados);
        return "presentation/administrador/management.html";
    }


}
