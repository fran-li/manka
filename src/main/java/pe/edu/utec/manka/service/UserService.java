package pe.edu.utec.manka.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utec.manka.dto.UserRegisterRequestDto;
import pe.edu.utec.manka.dto.UserRegisterResponseDto;
import pe.edu.utec.manka.dto.UserResponseDto;
import pe.edu.utec.manka.dto.UserUpdateRequestDto;
import pe.edu.utec.manka.entity.Usuario;
import pe.edu.utec.manka.exception.BusinessRuleException;
import pe.edu.utec.manka.exception.ResourceNotFoundException;
import pe.edu.utec.manka.repository.UsuarioRepository;

@Service
public class UserService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public UserRegisterResponseDto register(UserRegisterRequestDto request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        if (usuarioRepository.existsByEmail(normalizedEmail)) {
            throw new BusinessRuleException("Email already registered");
        }

        Usuario user = modelMapper.map(request, Usuario.class);
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Usuario saved = usuarioRepository.save(user);
        return new UserRegisterResponseDto(saved.getId());
    }

    @Transactional(readOnly = true)
    public UserResponseDto getProfile(String email) {
        Usuario user = findByEmail(email);
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Transactional
    public UserResponseDto updateProfile(String email, UserUpdateRequestDto request) {
        Usuario user = findByEmail(email);
        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        return modelMapper.map(usuarioRepository.save(user), UserResponseDto.class);
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }
}
