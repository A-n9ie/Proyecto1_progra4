package org.example.medicalappointment.presentation.usuarios;

import jakarta.servlet.http.HttpSession;
import org.example.medicalappointment.logic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.*;
import org.springframework.validation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@org.springframework.stereotype.Controller("usuarios")

public class ControllerUsuarios {
    @Autowired
    private ServiceUser serviceUser;
    @Autowired
    private ServiceDoctor serviceDoctor;
    @Autowired
    private ServicePatient servicePatient;

    @GetMapping("/presentation/usuarios/show")
    public String show(Model model) {
        model.addAttribute("usuarios", serviceUser.usuariosFindAll());
        return "/presentation/usuarios/register";
    }

    //Modelo vacio para colocar al inicio de la pagina
    @GetMapping("/presentation/usuarios/registerSys")
    public String register(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("persona", new Persona());
        return "/presentation/usuarios/register";
    }


    @PostMapping("/presentation/usuarios/create")
    public String create(@Valid @ModelAttribute Usuario usuario,
                         @Valid @ModelAttribute Persona persona,
                         BindingResult result,
                         @RequestParam("password_c") String passwordConfirm,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Error in the input data");
            return "/presentation/usuarios/register";
        }

        try {
            if ("Medico".equals(usuario.getRol()) && serviceDoctor.findDoctor(persona.getCedula()) != null) {
                throw new IllegalArgumentException("Doctor already exists");
            }

            if ("Paciente".equals(usuario.getRol()) && servicePatient.findPatient(persona.getCedula()) != null) {
                throw new IllegalArgumentException("Patient already exists");
            }

            serviceUser.addUser(usuario, passwordConfirm);

            usuario = serviceUser.getLastUser();
            persona.setUsuario(usuario);

            if ("Medico".equals(usuario.getRol())) {
                Medico doctor = new Medico(persona.getNombre(), persona.getCedula(), persona.getUsuario());
                serviceDoctor.addDoctor(doctor);
            } else {
                Paciente patient = new Paciente(persona.getNombre(), persona.getCedula(), persona.getUsuario());
                System.out.println(patient.getCedula());
                servicePatient.addPatient(patient);
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "/presentation/usuarios/register";
        }
        return "/presentation/usuarios/login";
    }

    @GetMapping("/presentation/perfil/show")
    public String profile(RedirectAttributes redirect) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = serviceUser.getUser(username);
        if (usuario.getRol().equals("Medico")) {
            redirect.addFlashAttribute("usuario", usuario);
            return "redirect:/presentation/doctor/profile";
        } else {
            redirect.addFlashAttribute("usuario", usuario);
            return "redirect:/presentation/patient/profile";
        }
    }

//    Login

    @GetMapping("/presentation/usuarios/login")
    public String showLoginPage() {
        return "/presentation/usuarios/login";
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        // Obtener el usuario autenticado desde SecurityContext
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        }

        // Guardar en la sesión si no está presente
        if (session.getAttribute("username") == null) {
            session.setAttribute("username", username);
        }

        // Pasar el nombre de usuario a la vista
        model.addAttribute("username", username);
        return "/presentation/fragments/fragments";
    }


}

