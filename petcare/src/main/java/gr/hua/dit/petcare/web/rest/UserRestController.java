package gr.hua.dit.petcare.web.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gr.hua.dit.petcare.core.model.UserType;
import gr.hua.dit.petcare.core.repository.UserRepository;
import gr.hua.dit.petcare.core.service.mapper.UserMapper;
import gr.hua.dit.petcare.core.service.model.UserView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "API for retrieving user information (e.g. Vets)")
@SecurityRequirement(name = "bearer-key")
/**
 * Endpoints to retrieve users information, such as registered veterinarians.
 */
public class UserRestController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserRestController(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Operation(summary = "List of Veterinarians", description = "Returns all registered veterinarians")
    @GetMapping("/vets")
    public ResponseEntity<List<UserView>> getAllVeterinarians() {
        List<UserView> vets = userRepository.findByUserType(UserType.VETERINARIAN)
                .stream()
                .map(userMapper::toView)
                .collect(Collectors.toList());

        return ResponseEntity.ok(vets);
    }
}