package br.com.alura.forum.config.security;

import java.security.Key;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {

    private static final long EXPIRATION_TIME = 86400000;
	
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String gerarToken(Authentication authenticate) {
        UserDetails userDetails = (UserDetails)authenticate.getPrincipal();
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);
      
        return Jwts.builder()
        .setIssuer("API do Fórum da Alura")
        .setSubject(userDetails.getUsername())
        .setIssuedAt(now)
        .setExpiration(expiration)
        .signWith(SECRET_KEY)
        .compact();
    }
    
    public boolean isTokenValido(String token) {
        JwtParser parser = Jwts.parserBuilder()
                      .setSigningKey(SECRET_KEY)
                      .build();
        try {
            parser.parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameUsuario(String token) {
        JwtParser parser = Jwts.parserBuilder()
                      .setSigningKey(SECRET_KEY)
                      .build();
        Claims claims = parser.parseClaimsJws(token).getBody(); 
        return claims.getSubject();          
    }
}
