package org.example.medicalappointment.presentation.usuarios;

import org.example.medicalappointment.logic.ServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller("usuarios")

public class ControllerUsuarios {
    @Autowired
    private ServiceUser serviceUser;

    @GetMapping("/presentation/usuarios/show")
    public String show(Model model) {
        model.addAttribute("usuarios", serviceUser.usuariosFindAll());
        return "presentation/usuarios/register";
    }

}
