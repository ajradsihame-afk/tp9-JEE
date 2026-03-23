package ma.fstg.security.demo.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// Indique que cette classe est un contrôleur Spring MVC
@Controller
public class HomeController {

    // Page d'accueil accessible à tous
    @GetMapping("/")
    public String home(Authentication authentication, Model model) {

        // Vérifie si l'utilisateur est connecté
        if (authentication != null) {

            // Récupère le nom de l'utilisateur connecté
            model.addAttribute("username", authentication.getName());

            // Récupère les rôles de l'utilisateur (USER ou ADMIN)
            model.addAttribute("roles", authentication.getAuthorities());
        }

        // Retourne la vue "home.html"
        return "home";
    }

    // Tableau de bord pour les utilisateurs (USER et ADMIN)
    @GetMapping("/user/dashboard")
    public String userDashboard(Authentication authentication, Model model) {

        // Ajoute le nom de l'utilisateur connecté à la vue
        model.addAttribute("username", authentication.getName());

        // Retourne la vue "user-dashboard.html"
        return "user-dashboard";
    }

    // Tableau de bord réservé aux administrateurs
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Authentication authentication, Model model) {

        // Ajoute le nom de l'utilisateur connecté à la vue
        model.addAttribute("username", authentication.getName());

        // Retourne la vue "admin-dashboard.html"
        return "admin-dashboard";
    }

    // Page de connexion personnalisée
    @GetMapping("/login")
    public String loginView() {

        // Retourne la vue "login.html"
        return "login";
    }

    // Page affichée en cas d'accès refusé
    @GetMapping("/access-denied")
    public String accessDeniedPage() {

        // Retourne la vue "access-denied.html"
        return "access-denied";
    }
}