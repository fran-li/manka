package pe.edu.utec.manka.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utec.manka.dto.HistorialRequestDto;
import pe.edu.utec.manka.dto.HistorialResponseDto;
import pe.edu.utec.manka.entity.HistorialCocina;
import pe.edu.utec.manka.entity.Plato;
import pe.edu.utec.manka.entity.Usuario;
import pe.edu.utec.manka.event.DishCookedEvent;
import pe.edu.utec.manka.exception.ResourceNotFoundException;
import pe.edu.utec.manka.repository.HistorialCocinaRepository;
import pe.edu.utec.manka.repository.PlatoRepository;
import pe.edu.utec.manka.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistorialService {
    private final HistorialCocinaRepository historialRepository;
    private final PlatoRepository platoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ApplicationEventPublisher eventPublisher;

    public HistorialService(HistorialCocinaRepository historialRepository,
                            PlatoRepository platoRepository,
                            UsuarioRepository usuarioRepository,
                            ApplicationEventPublisher eventPublisher) {
        this.historialRepository = historialRepository;
        this.platoRepository = platoRepository;
        this.usuarioRepository = usuarioRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public HistorialResponseDto create(String email, HistorialRequestDto request) {
        Usuario user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
        Plato dish = platoRepository.findById(request.getDishId())
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found"));

        HistorialCocina history = new HistorialCocina();
        history.setUsuario(user);
        history.setPlato(dish);
        history.setCocinadoEn(LocalDateTime.now());

        HistorialCocina saved = historialRepository.save(history);
        eventPublisher.publishEvent(new DishCookedEvent(
                this,
                saved.getId(),
                user.getEmail(),
                user.getFirstName(),
                dish.getId(),
                dish.getNombre(),
                saved.getCocinadoEn()
        ));
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<HistorialResponseDto> getAll(String email) {
        Usuario user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
        return historialRepository.findAllByUserId(user.getId()).stream().map(this::toResponse).toList();
    }

    private HistorialResponseDto toResponse(HistorialCocina history) {
        HistorialResponseDto response = new HistorialResponseDto();
        response.setId(history.getId());
        response.setDishId(history.getPlato().getId());
        response.setDishName(history.getPlato().getNombre());
        response.setCookedAt(history.getCocinadoEn());
        return response;
    }
}
