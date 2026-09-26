package pe.edu.utec.manka.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utec.manka.entity.Rol;
import pe.edu.utec.manka.entity.TipoRol;
import pe.edu.utec.manka.entity.Usuario;
import pe.edu.utec.manka.repository.RolRepository;
import pe.edu.utec.manka.repository.UsuarioRepository;

import java.util.HashSet;

@Component
public class RoleInitializer implements ApplicationRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${manka.admin.email:}")
    private String adminEmail;

    @Value("${manka.admin.password:}")
    private String adminPassword;

    @Value("${manka.admin.first-name:Manka}")
    private String adminFirstName;

    @Value("${manka.admin.last-name:Admin}")
    private String adminLastName;

    public RoleInitializer(
            RolRepository rolRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        Rol userRole = getOrCreateRole(TipoRol.USER);
        Rol adminRole = getOrCreateRole(TipoRol.ADMIN);

        usuarioRepository.findAll().stream()
                .filter(user -> user.getRoles().isEmpty())
                .forEach(user -> {
                    user.getRoles().add(userRole);
                    usuarioRepository.save(user);
                });

        if (!adminEmail.isBlank() && !adminPassword.isBlank()) {
            String normalizedEmail = adminEmail.trim().toLowerCase();

            Usuario admin = usuarioRepository.findByEmail(normalizedEmail)
                    .orElseGet(() -> {
                        Usuario newAdmin = new Usuario();
                        newAdmin.setFirstName(adminFirstName);
                        newAdmin.setLastName(adminLastName);
                        newAdmin.setEmail(normalizedEmail);
                        newAdmin.setPassword(passwordEncoder.encode(adminPassword));
                        newAdmin.setRoles(new HashSet<>());
                        return newAdmin;
                    });

            admin.getRoles().add(userRole);
            admin.getRoles().add(adminRole);
            usuarioRepository.save(admin);
        }
    }

    private Rol getOrCreateRole(TipoRol roleName) {
        return rolRepository.findByName(roleName)
                .orElseGet(() -> {
                    Rol role = new Rol();
                    role.setName(roleName);
                    return rolRepository.save(role);
                });
    }
}
