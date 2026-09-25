package pe.edu.utec.manka.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.utec.manka.AbstractContainerBaseTest;
import pe.edu.utec.manka.entity.Rol;
import pe.edu.utec.manka.entity.TipoRol;
import pe.edu.utec.manka.entity.Usuario;
import pe.edu.utec.manka.repository.RolRepository;
import pe.edu.utec.manka.repository.UsuarioRepository;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "manka.mail.enabled=false")
@AutoConfigureMockMvc
class SecurityAndRefreshTokenIntegrationTest extends AbstractContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void registeredUserReceivesUserRoleAndRefreshToken() throws Exception {
        Credentials credentials = registerUser();

        JsonNode login = login(credentials.email(), credentials.password());

        assertTrue(login.hasNonNull("token"));
        assertTrue(login.hasNonNull("refreshToken"));

        Jwt jwt = jwtDecoder.decode(login.get("token").asText());
        assertTrue(jwt.getClaimAsStringList("roles").contains("USER"));

        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer " + login.get("token").asText()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(credentials.email()))
                .andExpect(jsonPath("$.roles[0]").value("USER"));
    }

    @Test
    void refreshTokenRotatesAndOldTokenCannotBeReused() throws Exception {
        Credentials credentials = registerUser();
        JsonNode login = login(credentials.email(), credentials.password());

        String oldRefreshToken = login.get("refreshToken").asText();

        String refreshResponse = mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "refreshToken": "%s"
                                }
                                """.formatted(oldRefreshToken)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode refreshed = objectMapper.readTree(refreshResponse);

        assertNotEquals(login.get("token").asText(), refreshed.get("token").asText());
        assertNotEquals(oldRefreshToken, refreshed.get("refreshToken").asText());

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "refreshToken": "%s"
                                }
                                """.formatted(oldRefreshToken)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.title").value("Unauthorized"));
    }

    @Test
    void userCannotAccessAdminEndpoint() throws Exception {
        Credentials credentials = registerUser();
        JsonNode login = login(credentials.email(), credentials.password());

        mockMvc.perform(get("/admin/users")
                        .header("Authorization", "Bearer " + login.get("token").asText()))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanAccessAdminEndpoint() throws Exception {
        String email = "admin." + UUID.randomUUID() + "@manka.test";
        String password = "Admin123";

        Usuario admin = new Usuario();
        admin.setFirstName("Admin");
        admin.setLastName("Test");
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));

        Rol userRole = rolRepository.findByName(TipoRol.USER).orElseThrow();
        Rol adminRole = rolRepository.findByName(TipoRol.ADMIN).orElseThrow();
        admin.getRoles().add(userRole);
        admin.getRoles().add(adminRole);
        usuarioRepository.save(admin);

        JsonNode login = login(email, password);

        mockMvc.perform(get("/admin/users")
                        .header("Authorization", "Bearer " + login.get("token").asText()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void logoutRevokesRefreshToken() throws Exception {
        Credentials credentials = registerUser();
        JsonNode login = login(credentials.email(), credentials.password());

        String refreshToken = login.get("refreshToken").asText();

        mockMvc.perform(post("/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "refreshToken": "%s"
                                }
                                """.formatted(refreshToken)))
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "refreshToken": "%s"
                                }
                                """.formatted(refreshToken)))
                .andExpect(status().isUnauthorized());
    }

    private Credentials registerUser() throws Exception {
        String email = "security." + UUID.randomUUID() + "@manka.test";
        String password = "Password123";

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Security",
                                  "lastName": "Test",
                                  "email": "%s",
                                  "password": "%s"
                                }
                                """.formatted(email, password)))
                .andExpect(status().isCreated());

        return new Credentials(email, password);
    }

    private JsonNode login(String email, String password) throws Exception {
        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "%s",
                                  "password": "%s"
                                }
                                """.formatted(email, password)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response);
    }

    private record Credentials(String email, String password) {}
}
