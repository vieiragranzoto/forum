package br.com.alura.forum.controller.form;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class LoginForm {
    @NotNull @NotEmpty
    private String username;
    @NotNull @NotEmpty
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public UsernamePasswordAuthenticationToken converter() {
        return new UsernamePasswordAuthenticationToken(username, password);
    }
    
}
