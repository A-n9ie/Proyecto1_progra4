package org.example.medicalappointment.presentation.usuarios;

import org.example.medicalappointment.logic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.*;
import org.springframework.validation.*;
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
        model.addAttribute("persona", new Persona());
        return "presentation/usuarios/register";
    }

    @PostMapping("/presentation/usuarios/create")
    public String create(@Valid @ModelAttribute Usuario usuario,
                         @Valid @ModelAttribute Persona persona,
                         BindingResult result,
                         @RequestParam("password_c") String passwordConfirm,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Error in the input data");
            return "redirect:/presentation/usuarios/registerSys";
        }
        try {
            serviceUser.addUser(usuario, passwordConfirm);
            usuario = serviceUser.getLastUser();

            if ("Medico".equals(usuario.getRol())) {
                Medico doctor = new Medico(persona);
                redirectAttributes.addFlashAttribute("doctor", doctor);
                return "redirect:/presentation/medico/create";
            } else {
                Paciente patient = new Paciente(persona);
                redirectAttributes.addFlashAttribute("paciente", patient);
                return "redirect:/presentation/patient/create";
            }
        } catch (Exception e) {
            result.addError(new FieldError ("usuario", "username", e.getMessage()));
            return "redirect:/presentation/usuarios/registerSys";
        }
    }

    @GetMapping("/presentation/usuarios/profile")
    public String profile(RedirectAttributes redirect) {
        Usuario usuario = serviceUser.getUser("BBanner");
        if (usuario.getRol().equals("Medico")) {
            redirect.addFlashAttribute("usuario", usuario);
            return "redirect:/presentation/doctor/profile";
        }
        else{
            redirect.addFlashAttribute("usuario", usuario);
            return "redirect:/presentation/patient/profile";
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


        if (!usuario.getClave().equals(password)) {
            redirectAttributes.addFlashAttribute("error", "Credenciales incorrectas");
            return "redirect:/presentation/usuarios/login";
        }


        if ("Medico".equals(usuario.getRol())) {
            return "redirect:/"; //Hay que ver a donde tiene que llevarlos
        } else if ("Paciente".equals(usuario.getRol())) {
            return "redirect:/"; //Hay que ver a donde tiene que llevarlos
        }

        redirectAttributes.addFlashAttribute("error", "Rol no reconocido");
        return "redirect:/presentation/usuarios/login";
    }
}

