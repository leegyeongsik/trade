package edu.cnu.swacademy.security.auth.repository;

import edu.cnu.swacademy.security.auth.domain.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthenticationRepository extends JpaRepository<Authentication, Integer> {

    Optional<Authentication> findByRefreshToken(String refreshToken);
    Optional<Authentication> findByUserIdAndRefreshToken(int userId, String refreshToken);

    Optional<Authentication> findByUser_Id(Integer userId);
}