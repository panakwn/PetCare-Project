package gr.hua.dit.petcare.web.ui;

import gr.hua.dit.petcare.core.security.ApplicationUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        ApplicationUserDetails userDetails = (ApplicationUserDetails) authentication.getPrincipal();

        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("firstName", userDetails.getUser().getFirstName());
        model.addAttribute("userType", userDetails.getUser().getUserType().name()); // PET_OWNER ή VETERINARIAN

        return "home";
    }
}