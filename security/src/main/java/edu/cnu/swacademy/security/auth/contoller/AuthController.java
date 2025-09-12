package edu.cnu.swacademy.security.auth.contoller;

import edu.cnu.swacademy.security.auth.dto.LoginRequest;
import edu.cnu.swacademy.security.auth.dto.LoginResponse;
import edu.cnu.swacademy.security.auth.dto.TokenReissueRequest;
import edu.cnu.swacademy.security.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) throws Exception {
        return authService.login(request);
    }

    @PutMapping("/reissue")
    public LoginResponse reissue(@Valid @RequestBody TokenReissueRequest request) throws Exception {
        return authService.reissue(request);
    }
}