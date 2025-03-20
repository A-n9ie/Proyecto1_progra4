package org.example.medicalappointment.presentation.usuarios;

import org.example.medicalappointment.logic.ServiceUser;
import org.example.medicalappointment.logic.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.stereotype.Controller("usuarios")

public class ControllerUsuarios {
    @Autowired
    private ServiceUser serviceUser;

    @GetMapping("/presentation/usuarios/show")
    public String show(Model model) {
        model.addAttribute("usuarios", serviceUser.usuariosFindAll());
        return "presentation/usuarios/register";
    }

    @GetMapping("/presentation/usuarios/registerSys")
    public String register(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "presentation/usuarios/register";
    }

    @PostMapping("/presentation/usuarios/registerUsuario")
    public String registerUsuario(@ModelAttribute Usuario usuario, @RequestParam("password_c") String passwordConfirm) {
        if (!usuario.getPassword().equals(passwordConfirm)) {
            return "presentation/usuarios/register";
        }
        serviceUser.addUser(usuario.getUsername(), usuario.getPassword(), usuario.getRol());
        return "redirect:/presentation/usuarios/registerSys";
    }


}
