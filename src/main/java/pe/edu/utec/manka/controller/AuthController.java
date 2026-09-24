package pe.edu.utec.manka.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import pe.edu.utec.manka.dto.LoginRequestDto;
import pe.edu.utec.manka.dto.LoginResponseDto;
import pe.edu.utec.manka.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto request) {
        return authService.login(request);
    }
}
