package org.example.medicalappointment.security;

import org.example.medicalappointment.logic.Medico;
import org.example.medicalappointment.logic.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsImp implements UserDetails {
    private Usuario usuario;

    public UserDetailsImp(Usuario usuario) {this.usuario = usuario;}
    public Usuario getUsuario() {return usuario;}
    public void setUsuario(Usuario usuario) {this.usuario = usuario;}
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(usuario.getRol()));
        return authorities;
    }

    @Override
    public String getPassword() {return usuario.getClave();}

    @Override
    public String getUsername() {return usuario.getUsername();}

    @Override
    public boolean isAccountNonExpired() {return true;}

    @Override
    public boolean isAccountNonLocked() {return true;}

    @Override
    public boolean isCredentialsNonExpired() {return true;}

    public boolean isEnabled() {

        if ("Medico".equals(usuario.getRol())) {
            return usuario.getMedicos() != null &&
                    usuario.getMedicos().stream().anyMatch(Medico::getAprobado);
        }
        return true;
    }


}
