package gr.hua.dit.petcare.core.service.impl;

import gr.hua.dit.petcare.core.model.User;
import gr.hua.dit.petcare.core.model.UserType;
import gr.hua.dit.petcare.core.repository.UserRepository;
import gr.hua.dit.petcare.core.service.UserService;
import gr.hua.dit.petcare.core.service.mapper.UserMapper;
import gr.hua.dit.petcare.core.service.model.CreateUserRequest;
import gr.hua.dit.petcare.core.service.model.UserView;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(final UserRepository userRepository,
                           final UserMapper userMapper,
                           final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserView registerOwner(final CreateUserRequest createUserRequest) {
        if (createUserRequest == null) throw new IllegalArgumentException("Request cannot be null");

        final String username = createUserRequest.username().strip();
        final String email = createUserRequest.email().strip();
        final String passwordRaw = createUserRequest.password();
        final String firstName = createUserRequest.firstName().strip();
        final String lastName = createUserRequest.lastName().strip();
        final UserType userType = createUserRequest.userType();

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists.");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUserType(userType);
        user.setPassword(passwordEncoder.encode(passwordRaw));

        final User savedUser = userRepository.save(user);
        return userMapper.toView(savedUser);
    }

    @Override
    public UserView findByUsername(final String username) {
        final User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found."));
        return userMapper.toView(user);
    }

    @Override
    public List<UserView> findAllVets() {
        return userRepository.findByType(UserType.VETERINARIAN)
                .stream()
                .map(userMapper::toView)
                .collect(Collectors.toList());
    }

    @Override
    public UserView findById(final Long userId) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));
        return userMapper.toView(user);
    }
}