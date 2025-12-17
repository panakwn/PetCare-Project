package gr.hua.dit.petcare.web.ui;

import gr.hua.dit.petcare.core.model.UserType;
import gr.hua.dit.petcare.core.service.UserService;
import gr.hua.dit.petcare.core.service.model.CreateUserRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // --- LOGIN ---
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    // --- REGISTER ---
    @GetMapping("/register")
    public String register(Model model) {
        CreateUserRequest emptyRequest = new CreateUserRequest(
                "",             // username
                "",             // email
                "",             // password
                "",             // firstName
                "",             // lastName
                UserType.PET_OWNER  // userType (default)
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
}