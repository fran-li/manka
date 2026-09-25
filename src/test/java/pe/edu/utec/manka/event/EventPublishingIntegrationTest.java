package pe.edu.utec.manka.event;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import pe.edu.utec.manka.AbstractContainerBaseTest;
import pe.edu.utec.manka.dto.HistorialRequestDto;
import pe.edu.utec.manka.dto.UserRegisterRequestDto;
import pe.edu.utec.manka.entity.Usuario;
import pe.edu.utec.manka.repository.UsuarioRepository;
import pe.edu.utec.manka.service.FavoritoService;
import pe.edu.utec.manka.service.HistorialService;
import pe.edu.utec.manka.service.UserService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "manka.mail.enabled=false")
@RecordApplicationEvents
class EventPublishingIntegrationTest extends AbstractContainerBaseTest {

    @Autowired
    private UserService userService;

    @Autowired
    private HistorialService historialService;

    @Autowired
    private FavoritoService favoritoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void registeringUserPublishesUserRegisteredEvent(ApplicationEvents events) {
        UserRegisterRequestDto request = new UserRegisterRequestDto();
        request.setFirstName("Evento");
        request.setLastName("Registro");
        request.setEmail("event.register." + UUID.randomUUID() + "@manka.test");
        request.setPassword("Password123");

        userService.register(request);

        assertEquals(1, events.stream(UserRegisteredEvent.class).count());
    }

    @Test
    void creatingHistoryPublishesDishCookedEvent(ApplicationEvents events) {
        Usuario user = createUser("history");
        HistorialRequestDto request = new HistorialRequestDto();
        request.setDishId(1L);

        historialService.create(user.getEmail(), request);

        assertEquals(1, events.stream(DishCookedEvent.class).count());
    }

    @Test
    void addingFavoritePublishesFavoriteAddedEvent(ApplicationEvents events) {
        Usuario user = createUser("favorite");

        favoritoService.add(user.getEmail(), 1L);

        assertEquals(1, events.stream(FavoriteAddedEvent.class).count());
    }

    private Usuario createUser(String prefix) {
        Usuario user = new Usuario();
        user.setFirstName("Event");
        user.setLastName("Test");
        user.setEmail(prefix + "." + UUID.randomUUID() + "@manka.test");
        user.setPassword("encoded-not-needed-for-this-test");
        return usuarioRepository.save(user);
    }
}
