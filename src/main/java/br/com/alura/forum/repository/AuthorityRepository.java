package br.com.alura.forum.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.GrantedAuthority;

import br.com.alura.forum.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long>{

    Collection<GrantedAuthority> findByUsername(String username);
    
}
