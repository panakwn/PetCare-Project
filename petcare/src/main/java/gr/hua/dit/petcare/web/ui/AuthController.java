package gr.hua.dit.petcare.web.ui;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import gr.hua.dit.petcare.core.model.UserType;
import gr.hua.dit.petcare.core.service.UserService;
import gr.hua.dit.petcare.core.service.model.CreateUserRequest;
import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    
    @GetMapping("/login")
    public String login() {

        if (isAuthenticated()) {
            return "redirect:/";
        }
        return "login";
    }

    
    @GetMapping("/register")
    public String register(Model model) {
        
        if (isAuthenticated()) {
            return "redirect:/";
        }

        CreateUserRequest emptyRequest = new CreateUserRequest(
                "",             
                "",             
                "",             
                "",             
                "",             
                UserType.PET_OWNER  
        );

        model.addAttribute("user", emptyRequest);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") CreateUserRequest request,
                               BindingResult bindingResult,
                               Model model) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.registerOwner(request);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }

        return "redirect:/login";
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }
}