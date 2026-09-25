package pe.edu.utec.manka.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utec.manka.dto.PlatoIngredienteResponseDto;
import pe.edu.utec.manka.dto.PlatoResponseDto;
import pe.edu.utec.manka.entity.Plato;
import pe.edu.utec.manka.entity.PlatoIngrediente;
import pe.edu.utec.manka.exception.ResourceNotFoundException;
import pe.edu.utec.manka.repository.PlatoRepository;

import java.util.List;

@Service
public class PlatoService {
    private final PlatoRepository platoRepository;

    public PlatoService(PlatoRepository platoRepository) {
        this.platoRepository = platoRepository;
    }

    @Transactional(readOnly = true)
    public List<PlatoResponseDto> search(String search, Integer maxMinutes) {
        List<Plato> dishes;
        if (maxMinutes != null) {
            dishes = platoRepository.findCandidates(maxMinutes);
            if (search != null && !search.isBlank()) {
                String normalized = search.trim().toLowerCase();
                dishes = dishes.stream()
                        .filter(dish -> dish.getNombre().toLowerCase().contains(normalized))
                        .toList();
            }
        } else if (search != null && !search.isBlank()) {
            dishes = platoRepository.findByNombreContainingIgnoreCaseOrderByNombreAsc(search.trim());
        } else {
            dishes = platoRepository.findAll();
        }

        return dishes.stream().map(dish -> toResponse(dish, true)).toList();
    }

    @Transactional(readOnly = true)
    public PlatoResponseDto getById(Long id) {
        Plato dish = platoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found"));
        return toResponse(dish, true);
    }

    public PlatoResponseDto toResponse(Plato dish, boolean includeIngredients) {
        PlatoResponseDto response = new PlatoResponseDto();
        response.setId(dish.getId());
        response.setNombre(dish.getNombre());
        response.setTiempoPreparacion(dish.getTiempoPreparacion());
        response.setDificultad(dish.getDificultad());
        response.setPopularidad(dish.getPopularidad());
        response.setPreparacion(dish.getPreparacion());
        if (dish.getCategoriaProteina() != null) {
            response.setCategoriaProteina(dish.getCategoriaProteina().getNombre());
        }
        if (includeIngredients) {
            response.setIngredientes(dish.getIngredientes().stream().map(this::ingredientToResponse).toList());
        }
        return response;
    }

    private PlatoIngredienteResponseDto ingredientToResponse(PlatoIngrediente item) {
        PlatoIngredienteResponseDto response = new PlatoIngredienteResponseDto();
        response.setIngredienteId(item.getIngrediente().getId());
        response.setNombre(item.getIngrediente().getNombre());
        response.setCantidad(item.getCantidadTexto());
        response.setObligatorio(item.getObligatorio());
        return response;
    }
}
