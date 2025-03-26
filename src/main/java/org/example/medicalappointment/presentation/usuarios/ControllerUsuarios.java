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
    @Autowired
    private ServiceDoctor serviceDoctor;
    @Autowired
    private ServicePatient servicePatient;

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
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Error in the input data");
            return "presentation/usuarios/register";
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
            return "presentation/usuarios/register";
        }
        return "presentation/usuarios/login";
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


//Este metodo no sirve y deberia ser el spring que lo haga, o ver como logear bien
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        RedirectAttributes redirectAttributes) {


        if (username == null || username.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El campo de usuario no puede estar vacío");
            return "redirect:/presentation/usuarios/login";
        }

        if (password == null || password.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El campo de contraseña no puede estar vacío");
            return "redirect:/presentation/usuarios/login";
        }

        Usuario usuario = serviceUser.getUser(username);

        if (usuario == null || !usuario.getClave().equals(password)) {
            redirectAttributes.addFlashAttribute("error", "Usuario o contraseña incorrectos");
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

