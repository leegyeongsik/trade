package edu.cnu.swacademy.security.common;

import edu.cnu.swacademy.security.auth.entity.Authentication;
import edu.cnu.swacademy.security.user.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class Validate {

    public void passwordValidation(String a , String b) throws SecurityException {
        if(!a.equals(b)){
            throw new SecurityException(ErrorCode.INVALID_CREDENTIALS);
        }
    }
    public User isUser(Optional<User> user) throws SecurityException {
        return user.orElseThrow(() ->
                  new SecurityException(ErrorCode.USER_NOT_FOUND));
    }
    public Authentication isAuthentication(Optional<Authentication> authentication) throws SecurityException {
        return authentication.orElseThrow(() ->
                new SecurityException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

    }
    public void authenticationValidation(Authentication authenticationToken) throws SecurityException {
        if (authenticationToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new SecurityException(ErrorCode.UNAUTHORIZED) ;
        }
    }

    public void existsByEmail(boolean b) throws SecurityException {
        if(b){
            throw new SecurityException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }
}
