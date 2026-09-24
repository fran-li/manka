package pe.edu.utec.manka.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utec.manka.dto.PlatoResponseDto;
import pe.edu.utec.manka.entity.Plato;
import pe.edu.utec.manka.entity.Usuario;
import pe.edu.utec.manka.exception.ConflictException;
import pe.edu.utec.manka.exception.ResourceNotFoundException;
import pe.edu.utec.manka.repository.PlatoRepository;
import pe.edu.utec.manka.repository.UsuarioRepository;

import java.util.List;

@Service
public class FavoritoService {

    private final UsuarioRepository usuarioRepository;
    private final PlatoRepository platoRepository;
    private final PlatoService platoService;

    public FavoritoService(
            UsuarioRepository usuarioRepository,
            PlatoRepository platoRepository,
            PlatoService platoService) {

        this.usuarioRepository = usuarioRepository;
        this.platoRepository = platoRepository;
        this.platoService = platoService;
    }

    @Transactional
    public void add(String email, Long dishId) {

        Usuario user = usuarioRepository.findWithFavoritosByEmail(email).orElseThrow(()
                -> new ResourceNotFoundException("Authenticated user not found"));

        Plato dish = platoRepository.findById(dishId).orElseThrow(()
                -> new ResourceNotFoundException("Dish not found"));

        boolean alreadyFavorite = user.getFavoritos().stream()
                                  .anyMatch(favorite -> favorite.getId().equals(dishId));

        if (alreadyFavorite) {
            throw new ConflictException("Dish is already in favorites");
        }

        user.getFavoritos().add(dish);
        usuarioRepository.save(user);
    }

    @Transactional
    public void remove(String email, Long dishId) {

        Usuario user = usuarioRepository.findWithFavoritosByEmail(email).orElseThrow(()
                -> new ResourceNotFoundException("Authenticated user not found"));

        user.getFavoritos().removeIf(dish -> dish.getId().equals(dishId));
        usuarioRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<PlatoResponseDto> getAll(String email) {

        Usuario user = usuarioRepository.findWithFavoritosByEmail(email).orElseThrow(()
                -> new ResourceNotFoundException("Authenticated user not found"));

        return user.getFavoritos().stream().sorted((a, b)
               -> a.getNombre().compareToIgnoreCase(b.getNombre()))
               .map(dish -> platoService.toResponse(dish, true)).toList();
    }
}