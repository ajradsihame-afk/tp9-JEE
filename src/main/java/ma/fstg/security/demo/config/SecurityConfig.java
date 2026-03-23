package ma.fstg.security.demo.config;

// Import des classes nécessaires pour la configuration Spring Security
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// Indique que cette classe est une classe de configuration Spring
@Configuration
public class SecurityConfig {

    // Définition des utilisateurs (en mémoire, sans base de données)
    @Bean
    public UserDetailsService userDetailsService() {

        // Création d'un utilisateur avec rôle ADMIN
        UserDetails admin = User.withUsername("admin")   // nom d'utilisateur
                .password("{noop}admin123")              // mot de passe (noop = non crypté)
                .roles("ADMIN")                          // rôle
                .build();

        // Création d'un utilisateur avec rôle USER
        UserDetails user = User.withUsername("user")
                .password("{noop}user123")
                .roles("USER")
                .build();

        // Stockage des utilisateurs en mémoire
        return new InMemoryUserDetailsManager(admin, user);
    }

    // Configuration principale de la sécurité HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Configuration des autorisations d'accès
                .authorizeHttpRequests(auth -> auth

                        // Pages accessibles sans authentification
                        .requestMatchers("/", "/login", "/access-denied").permitAll()

                        // Accès réservé uniquement aux ADMIN pour les URLs /admin/**
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Accès aux USER et ADMIN pour les URLs /user/**
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")

                        // Toute autre requête nécessite une authentification
                        .anyRequest().authenticated()
                )

                // Configuration du formulaire de connexion
                .formLogin(form -> form

                        // Page personnalisée de login
                        .loginPage("/login")

                        // Redirection après succès de connexion
                        .defaultSuccessUrl("/", true)

                        // Redirection en cas d'erreur de login
                        .failureUrl("/login?error=true")

                        // Autoriser tout le monde à accéder à la page login
                        .permitAll()
                )

                // Configuration de la déconnexion
                .logout(logout -> logout

                        // URL pour déclencher le logout
                        .logoutUrl("/logout")

                        // Redirection après déconnexion
                        .logoutSuccessUrl("/login?logout=true")

                        // Invalidation de la session
                        .invalidateHttpSession(true)

                        // Suppression des cookies de session
                        .deleteCookies("JSESSIONID")

                        // Autoriser tout le monde à se déconnecter
                        .permitAll()
                )

                // Gestion des erreurs d'autorisation (accès refusé)
                .exceptionHandling(ex -> ex

                        // Page affichée si accès interdit
                        .accessDeniedPage("/access-denied")
                )

                // Désactivation de la protection CSRF (à utiliser seulement pour tests)
                .csrf(csrf -> csrf.disable());

        // Retourne la configuration construite
        return http.build();
    }
}