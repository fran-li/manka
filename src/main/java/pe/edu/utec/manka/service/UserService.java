package pe.edu.utec.manka.service;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utec.manka.dto.UserRegisterRequestDto;
import pe.edu.utec.manka.dto.UserRegisterResponseDto;
import pe.edu.utec.manka.dto.UserResponseDto;
import pe.edu.utec.manka.dto.UserUpdateRequestDto;
import pe.edu.utec.manka.entity.Rol;
import pe.edu.utec.manka.entity.TipoRol;
import pe.edu.utec.manka.entity.Usuario;
import pe.edu.utec.manka.event.UserRegisteredEvent;
import pe.edu.utec.manka.exception.ResourceNotFoundException;
import pe.edu.utec.manka.exception.RoleNotFoundException;
import pe.edu.utec.manka.exception.UserAlreadyExistsException;
import pe.edu.utec.manka.repository.RolRepository;
import pe.edu.utec.manka.repository.UsuarioRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;

    public UserService(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder,
            ModelMapper modelMapper,
            ApplicationEventPublisher eventPublisher) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public UserRegisterResponseDto register(UserRegisterRequestDto request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        if (usuarioRepository.existsByEmail(normalizedEmail)) {
            throw new UserAlreadyExistsException("Email already registered");
        }

        Rol userRole = rolRepository.findByName(TipoRol.USER)
                .orElseThrow(() -> new RoleNotFoundException("USER role not found"));

        Usuario user = modelMapper.map(request, Usuario.class);
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.getRoles().add(userRole);

        Usuario saved = usuarioRepository.save(user);

        eventPublisher.publishEvent(new UserRegisteredEvent(
                this,
                saved.getId(),
                saved.getEmail(),
                saved.getFirstName()
        ));

        return new UserRegisterResponseDto(saved.getId());
    }

    @Transactional(readOnly = true)
    public UserResponseDto getProfile(String email) {
        return toResponse(findByEmail(email));
    }

    @Transactional
    public UserResponseDto updateProfile(String email, UserUpdateRequestDto request) {
        Usuario user = findByEmail(email);
        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        return toResponse(usuarioRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public UserResponseDto updateRoles(Long userId, Set<TipoRol> roleNames) {
        Usuario user = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Set<Rol> roles = roleNames.stream()
                .map(roleName -> rolRepository.findByName(roleName)
                        .orElseThrow(() -> new RoleNotFoundException(roleName + " role not found")))
                .collect(Collectors.toSet());

        user.setRoles(roles);
        return toResponse(usuarioRepository.save(user));
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }

    private UserResponseDto toResponse(Usuario user) {
        UserResponseDto response = modelMapper.map(user, UserResponseDto.class);
        response.setRoles(user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet()));
        return response;
    }
}
