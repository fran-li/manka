package pe.edu.utec.manka.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utec.manka.dto.IngredienteResponseDto;
import pe.edu.utec.manka.entity.Ingrediente;
import pe.edu.utec.manka.repository.IngredienteRepository;

import java.util.List;

@Service
public class IngredienteService {
    private final IngredienteRepository ingredienteRepository;

    public IngredienteService(IngredienteRepository ingredienteRepository) {
        this.ingredienteRepository = ingredienteRepository;
    }

    @Transactional(readOnly = true)
    public List<IngredienteResponseDto> search(String search) {
        List<Ingrediente> ingredients = search == null || search.isBlank()
                ? ingredienteRepository.findAll()
                : ingredienteRepository.findByNombreContainingIgnoreCaseOrderByNombreAsc(search.trim());
        return ingredients.stream().map(this::toResponse).toList();
    }

    public IngredienteResponseDto toResponse(Ingrediente ingredient) {
        IngredienteResponseDto response = new IngredienteResponseDto();
        response.setId(ingredient.getId());
        response.setNombre(ingredient.getNombre());
        if (ingredient.getParent() != null) {
            response.setParentId(ingredient.getParent().getId());
            response.setParentNombre(ingredient.getParent().getNombre());
        }
        return response;
    }
}
