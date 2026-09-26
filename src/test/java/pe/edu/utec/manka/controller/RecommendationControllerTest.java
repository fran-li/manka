package pe.edu.utec.manka.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.utec.manka.AbstractContainerBaseTest;
import pe.edu.utec.manka.entity.HistorialCocina;
import pe.edu.utec.manka.entity.Plato;
import pe.edu.utec.manka.entity.Usuario;
import pe.edu.utec.manka.repository.HistorialCocinaRepository;
import pe.edu.utec.manka.repository.PlatoRepository;
import pe.edu.utec.manka.repository.UsuarioRepository;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RecommendationControllerTest extends AbstractContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PlatoRepository platoRepository;

    @Autowired
    private HistorialCocinaRepository historialCocinaRepository;

    @Test
    void shouldReturnRankedRecommendationsWithinThirtyMinutesAndCoverageAboveZero() throws Exception {
        Usuario user = createUser();

        String response = mockMvc.perform(post("/recommendations")
                        .with(jwt().jwt(jwt -> jwt.subject(user.getEmail())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "ingredientIds": [1, 4],
                                  "availableMinutes": 30,
                                  "limit": 10
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode data = objectMapper.readTree(response);
        assertTrue(data.isArray());
        assertTrue(data.size() > 0);

        double previousScore = Double.MAX_VALUE;
        for (JsonNode recommendation : data) {
            assertTrue(recommendation.get("tiempoPreparacion").asInt() <= 30);
            assertTrue(recommendation.get("ingredientCoveragePercent").asDouble() > 0.0);
            assertTrue(recommendation.get("matchedIngredients").asInt() > 0);

            double currentScore = recommendation.get("score").asDouble();
            assertTrue(currentScore <= previousScore);
            previousScore = currentScore;
        }

        JsonNode first = data.get(0);
        assertEquals(1L, first.get("platoId").asLong());
        assertEquals("Arroz chaufa de pollo", first.get("nombre").asText());
        assertEquals(2, first.get("matchedIngredients").asInt());
        assertEquals(6, first.get("totalIngredients").asInt());
        assertEquals(33.33, first.get("ingredientCoveragePercent").asDouble(), 0.01);
    }

    @Test
    void shouldAllowSixtyMinuteCandidates() throws Exception {
        Usuario user = createUser();

        String response = mockMvc.perform(post("/recommendations")
                        .with(jwt().jwt(jwt -> jwt.subject(user.getEmail())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "ingredientIds": [1, 4],
                                  "availableMinutes": 60,
                                  "limit": 20
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode data = objectMapper.readTree(response);
        boolean hasSixtyMinuteDish = false;

        for (JsonNode recommendation : data) {
            assertTrue(recommendation.get("tiempoPreparacion").asInt() <= 60);
            assertTrue(recommendation.get("ingredientCoveragePercent").asDouble() > 0.0);
            if (recommendation.get("tiempoPreparacion").asInt() == 60) {
                hasSixtyMinuteDish = true;
            }
        }

        assertTrue(hasSixtyMinuteDish);
    }

    @Test
    void shouldPenalizeRecentlyCookedDish() throws Exception {
        Usuario user = createUser();
        Plato chaufa = platoRepository.findById(1L).orElseThrow();

        HistorialCocina history = new HistorialCocina();
        history.setUsuario(user);
        history.setPlato(chaufa);
        history.setCocinadoEn(LocalDateTime.now());
        historialCocinaRepository.save(history);

        String response = mockMvc.perform(post("/recommendations")
                        .with(jwt().jwt(jwt -> jwt.subject(user.getEmail())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "ingredientIds": [1, 4, 5, 6, 7, 8],
                                  "availableMinutes": 20,
                                  "limit": 20
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode data = objectMapper.readTree(response);
        JsonNode chaufaResult = null;

        for (JsonNode recommendation : data) {
            if (recommendation.get("platoId").asLong() == 1L) {
                chaufaResult = recommendation;
                break;
            }
        }

        assertNotNull(chaufaResult);
        assertEquals(0.0, chaufaResult.get("breakdown").get("repetition").asDouble(), 0.01);
        assertTrue(chaufaResult.get("reasons").toString().toLowerCase().contains("repetición"));
    }

    private Usuario createUser() {
        Usuario user = new Usuario();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("recommendation." + UUID.randomUUID() + "@manka.test");
        user.setPassword("encoded-not-needed-for-this-test");
        return usuarioRepository.save(user);
    }
}
