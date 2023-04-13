package it.AziendaAgricolaBack.auth.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import it.AziendaAgricolaBack.entities.Utente;
import it.AziendaAgricolaBack.repos.UtenteRepo;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UtenteRepo userRepository;

    public CustomUserDetailsService(UtenteRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String nameOrEmail) throws UsernameNotFoundException {
          Utente user = userRepository.findByNomeOrEmail(nameOrEmail, nameOrEmail)
                 .orElseThrow(() ->
                         new UsernameNotFoundException("User not found with name or email: "+ nameOrEmail));

        Set<GrantedAuthority> authorities = user
                .getRoles()
                .stream()
                .map((role) -> new SimpleGrantedAuthority(role.getRoleName().toString())).collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                authorities);
    }
}

