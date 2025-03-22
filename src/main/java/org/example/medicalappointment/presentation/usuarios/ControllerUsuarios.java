package org.example.medicalappointment.presentation.usuarios;

import org.example.medicalappointment.logic.Medico;
import org.example.medicalappointment.logic.Paciente;
import org.example.medicalappointment.logic.ServiceUser;
import org.example.medicalappointment.logic.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@org.springframework.stereotype.Controller("usuarios")

public class ControllerUsuarios {
    @Autowired
    private ServiceUser serviceUser;

    @GetMapping("/presentation/usuarios/show")
    public String show(Model model) {
        model.addAttribute("usuarios", serviceUser.usuariosFindAll());
        return "presentation/usuarios/register";
    }

//Modelo vacio para colocar al inicio de la pagina
    @GetMapping("/presentation/usuarios/registerSys")
    public String register(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "presentation/doctor/profile";
    }

    @PostMapping("/presentation/usuarios/registerUsuario")
    public String registerUsuario(@ModelAttribute Usuario usuario,
                                  @RequestParam("password_c") String passwordConfirm,
                                  @RequestParam("cedula") String cedula,
                                  @RequestParam("nombre") String nombre,
                                  RedirectAttributes redirectAttributes) {

        if (!usuario.getPassword().equals(passwordConfirm)) {
            redirectAttributes.addFlashAttribute("error", "Password does not match");
            return "redirect:/presentation/usuarios/registerSys";
        } else if(serviceUser.getUser(usuario.getUsername()) != null) {
            redirectAttributes.addFlashAttribute("error", "Username already exists");
            return "redirect:/presentation/usuarios/registerSys";
        } else if(serviceUser.getPaciente(cedula) != null && usuario.getRol().equals("Paciente")) {
            redirectAttributes.addFlashAttribute("error", "Patient already exists");
            return "redirect:/presentation/usuarios/registerSys";
        } else if (serviceUser.getMedico(cedula) != null  && usuario.getRol().equals("Medico") ) {
            redirectAttributes.addFlashAttribute("error", "Doctor already exists");
            return "redirect:/presentation/usuarios/registerSys";
        }
        else {
            serviceUser.addUser(usuario.getUsername(), usuario.getPassword(), usuario.getRol());

            usuario = serviceUser.getLastUser();

            if ("Medico".equals(usuario.getRol())) {
                Medico doctor = new Medico();
                doctor.setCedula(cedula);
                doctor.setNombre(nombre);
                doctor.setUsuario(usuario);
                redirectAttributes.addFlashAttribute("doctor", doctor);
                return "redirect:/presentation/medico/doctorRegister";
            } else {
                Paciente patient = new Paciente();
                patient.setCedula(cedula);
                patient.setNombre(nombre);
                patient.setUsuario(usuario);
                redirectAttributes.addFlashAttribute("paciente", patient);
                return "redirect:/presentation/patient/patientRegister";
            }
        }
    }


//    Login

    @GetMapping("/presentation/usuarios/login")
    public String showLoginPage() {
        return "presentation/usuarios/login";
    }

    @PostMapping("/presentation/usuarios/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        RedirectAttributes redirectAttributes) {


        Usuario usuario = serviceUser.getUser(username);

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/presentation/usuarios/login";
        }


        if (!usuario.getPassword().equals(password)) {
            redirectAttributes.addFlashAttribute("error", "Credenciales incorrectas");
            return "redirect:/presentation/usuarios/login";
        }


        if ("Medico".equals(usuario.getRol())) {
            return "redirect:/presentation/principal/index"; //Hay que ver a donde tiene que llevarlos
        } else if ("Paciente".equals(usuario.getRol())) {
            return "redirect:/presentation/principal/index"; //Hay que ver a donde tiene que llevarlos
        }

        redirectAttributes.addFlashAttribute("error", "Rol no reconocido");
        return "redirect:/presentation/usuarios/login";
    }
}