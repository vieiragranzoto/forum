package br.com.alura.forum.config.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.forum.model.User;
import br.com.alura.forum.repository.AuthorityRepository;
import br.com.alura.forum.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter{

    private TokenService tokenService;
    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;

    public AutenticacaoViaTokenFilter(TokenService tokenService, UserRepository userRepository,
            AuthorityRepository authorityRepository) {
                this.tokenService = tokenService;
                this.userRepository = userRepository;
                this.authorityRepository = authorityRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                
                String token = recuperarToken(request);

                boolean valido = tokenService.isTokenValido(token);
                if(valido){
                    autenticarCliente(token);
                }

                filterChain.doFilter(request, response);
    }

    private void autenticarCliente(String token) {
        String username = tokenService.getUsernameUsuario(token);
        Optional<User> optional = userRepository.findById(username);
        User usuario = optional.get();
        Collection<GrantedAuthority> authorities = authorityRepository.findByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, token, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String recuperarToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token==null || token.isEmpty() || !token.startsWith("Bearer ")){
            return null;
        }
        return token.substring(7, token.length());
    }
    
}
