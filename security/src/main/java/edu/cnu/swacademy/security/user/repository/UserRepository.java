package edu.cnu.swacademy.security.user.repository;

import edu.cnu.swacademy.security.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
  boolean existsByEmail(String email);

    Optional<User> findByEmail(@NotBlank @Email @Size(min = 10, max = 100) String email);
}
