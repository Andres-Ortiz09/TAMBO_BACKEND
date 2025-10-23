package com.proy.utp.backend_tambo.service;

import com.proy.utp.backend_tambo.model.User;
import com.proy.utp.backend_tambo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public List<User> findAll() {
        return repo.findAll();
    }

    public User findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public User create(User u) {
        if (repo.existsByEmail(u.getEmail())) {
            throw new RuntimeException("El correo ya está registrado");
        }
        u.setPassword(encoder.encode(u.getPassword()));
        return repo.save(u);
    }

    public User update(Long id, User u) {
        User existing = findById(id);
        existing.setFirstName(u.getFirstName());
        existing.setLastName(u.getLastName());
        existing.setAddress(u.getAddress());
        existing.setPhone(u.getPhone());
        return repo.save(existing);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
