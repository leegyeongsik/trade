package edu.cnu.swacademy.security.auth.service;

import edu.cnu.swacademy.security.auth.dto.LoginRequest;
import edu.cnu.swacademy.security.auth.dto.LoginResponse;
import edu.cnu.swacademy.security.auth.dto.TokenReissueRequest;
import edu.cnu.swacademy.security.auth.domain.Authentication;
import edu.cnu.swacademy.security.auth.repository.AuthenticationRepository;
import edu.cnu.swacademy.security.common.*;
import edu.cnu.swacademy.security.common.SecurityException;
import edu.cnu.swacademy.security.user.entity.User;
import edu.cnu.swacademy.security.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationRepository authenticationRepository;
    private final JwtUtil jwtUtil;
    private final Validate validate;
    public AuthService(UserRepository userRepository,
                       AuthenticationRepository authenticationRepository,
                       JwtUtil jwtUtil, Validate validate) {
        this.userRepository = userRepository;
        this.authenticationRepository = authenticationRepository;
        this.jwtUtil = jwtUtil;
        this.validate = validate;
    }
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(LoginRequest request) throws SecurityException {
        User user = validate.isUser(userRepository.findByEmail(request.getUserEmail()));
        String encodingPassword =  HashUtil.sha512(request.getUserPassword());
        validate.passwordValidation(encodingPassword,user.getPassword());

        TokenInfo tokenInfo= jwtUtil.generateAccessAndRefreshToken(user.getId());
        Authentication authentication=validate.checkAuthentication(authenticationRepository.findByUser(user)); // 있으면 그거 그냥 가져와서 업데이트만 쳐
        authentication =  authentication == null
                ? new Authentication(user,tokenInfo.refreshToken(),tokenInfo.refreshTokenExpiredAt()) :
                authentication.updateRefreshToken(tokenInfo.refreshToken(),tokenInfo.refreshTokenExpiredAt());
        authenticationRepository.save(authentication);

        return new LoginResponse(tokenInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    public LoginResponse reissue(TokenReissueRequest request) throws SecurityException {
        Authentication authentication =  validate.isRefreshTokenAuthentication(authenticationRepository.findByRefreshToken(request.refreshToken()));
        User user = validate.isUser(userRepository.findById(authentication.getUser().getId()));
        validate.authenticationValidation(authentication);

        TokenInfo tokenInfo= jwtUtil.generateAccessAndRefreshToken(user.getId());
        authentication.updateRefreshToken(tokenInfo.refreshToken(),tokenInfo.refreshTokenExpiredAt());

        authenticationRepository.save(authentication);

        return new LoginResponse(new TokenInfo(authentication.getRefreshToken()
                ,authentication.getExpiredAt(),
                authentication.getRefreshToken(),
                authentication.getExpiredAt().plusWeeks(1)));
    }
}
