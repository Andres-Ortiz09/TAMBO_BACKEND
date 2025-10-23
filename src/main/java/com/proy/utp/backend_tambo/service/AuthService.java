package com.proy.utp.backend_tambo.service;

import com.proy.utp.backend_tambo.dto.UserResponse;
import com.proy.utp.backend_tambo.model.*;
import com.proy.utp.backend_tambo.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
    }

    public UserResponse registerUser(User user) {
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        // Cifrrado de contraseña
        user.setPassword(encoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<>();
        if (user.getEmail().toLowerCase().endsWith("@tambo.com")) {
            Role admin = roleRepo.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));
            roles.add(admin);
        } else {
            Role roleUser = roleRepo.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
            roles.add(roleUser);
        }

        user.setRoles(roles);
        User savedUser = userRepo.save(user);

        Set<String> roleNames = new HashSet<>();
        savedUser.getRoles().forEach(r -> roleNames.add(r.getName()));

        return new UserResponse(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getAddress(),
                savedUser.getPhone(),
                savedUser.getPassword(), 
                roleNames
        );
    }
}
