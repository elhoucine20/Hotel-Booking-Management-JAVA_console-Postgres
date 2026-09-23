package repository.impl;

import model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    public boolean save(User user);
    public Optional<User> findByEmail(String email);
    public boolean existsByEmail(String email);
    public List<User> findAll();
    boolean updateProfile(UUID userId, String fullName, String email);

}
