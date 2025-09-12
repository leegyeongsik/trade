package edu.cnu.swacademy.security.user.service;

import edu.cnu.swacademy.security.common.ErrorCode;
import edu.cnu.swacademy.security.common.Validate;
import edu.cnu.swacademy.security.user.dto.UserSignupRequest;
import edu.cnu.swacademy.security.user.entity.User;
import edu.cnu.swacademy.security.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService {
  private final Validate validate;
  private final UserRepository userRepository;

  public UserService(Validate validate, UserRepository userRepository) {
      this.validate = validate;
      this.userRepository = userRepository;
  }

  @Transactional
  public int signUp(UserSignupRequest userSignupRequest) throws edu.cnu.swacademy.security.common.SecurityException {
    validate.existsByEmail(userRepository.existsByEmail(userSignupRequest.getUserEmail()));

    User savedUser = userRepository.save(
        new User(
            userSignupRequest.getUserName(),
            userSignupRequest.getUserEmail(),
            userSignupRequest.getUserPassword()
        )
    );
    log.info("Completed sign up. user-id : {}", savedUser.getId());

    return savedUser.getId();
  }
}
