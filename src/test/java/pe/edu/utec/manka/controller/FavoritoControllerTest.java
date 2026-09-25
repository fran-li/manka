package pe.edu.utec.manka.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.utec.manka.AbstractContainerBaseTest;
import pe.edu.utec.manka.entity.Usuario;
import pe.edu.utec.manka.repository.UsuarioRepository;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FavoritoControllerTest extends AbstractContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void shouldCreateFavoriteAndReturnDishWithIngredients() throws Exception {
        Usuario user = createUser();

        mockMvc.perform(post("/favorites/1")
                        .with(jwt().jwt(jwt -> jwt.subject(user.getEmail()))))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/favorites")
                        .with(jwt().jwt(jwt -> jwt.subject(user.getEmail()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Arroz chaufa de pollo"))
                .andExpect(jsonPath("$[0].ingredientes").isArray())
                .andExpect(jsonPath("$[0].ingredientes.length()").value(6));
    }

    @Test
    void shouldReturnConflictWhenFavoriteIsDuplicated() throws Exception {
        Usuario user = createUser();

        mockMvc.perform(post("/favorites/1")
                        .with(jwt().jwt(jwt -> jwt.subject(user.getEmail()))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/favorites/1")
                        .with(jwt().jwt(jwt -> jwt.subject(user.getEmail()))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.title").value("Conflict"))
                .andExpect(jsonPath("$.detail").value("Dish is already in favorites"));
    }

    @Test
    void shouldReturnNotFoundWhenDishDoesNotExist() throws Exception {
        Usuario user = createUser();

        mockMvc.perform(post("/favorites/999")
                        .with(jwt().jwt(jwt -> jwt.subject(user.getEmail()))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("Dish not found"));
    }

    private Usuario createUser() {
        Usuario user = new Usuario();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("favorite." + UUID.randomUUID() + "@manka.test");
        user.setPassword("encoded-not-needed-for-this-test");
        return usuarioRepository.save(user);
    }
}
