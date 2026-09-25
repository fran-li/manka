package pe.edu.utec.manka.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utec.manka.dto.DespensaRequestDto;
import pe.edu.utec.manka.dto.IngredienteResponseDto;
import pe.edu.utec.manka.entity.Ingrediente;
import pe.edu.utec.manka.entity.Usuario;
import pe.edu.utec.manka.exception.BusinessRuleException;
import pe.edu.utec.manka.exception.ResourceNotFoundException;
import pe.edu.utec.manka.repository.IngredienteRepository;
import pe.edu.utec.manka.repository.UsuarioRepository;

import java.util.List;
import java.util.Set;

@Service
public class DespensaService {
    private final UsuarioRepository usuarioRepository;
    private final IngredienteRepository ingredienteRepository;
    private final IngredienteService ingredienteService;

    public DespensaService(UsuarioRepository usuarioRepository,
                           IngredienteRepository ingredienteRepository,
                           IngredienteService ingredienteService) {
        this.usuarioRepository = usuarioRepository;
        this.ingredienteRepository = ingredienteRepository;
        this.ingredienteService = ingredienteService;
    }

    @Transactional
    public List<IngredienteResponseDto> replace(String email, DespensaRequestDto request) {
        Usuario user = usuarioRepository.findWithDespensaByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        Set<Long> ids = request.getIngredientIds();
        List<Ingrediente> ingredients = ingredienteRepository.findAllById(ids);
        if (ingredients.size() != ids.size()) {
            throw new BusinessRuleException("One or more ingredientIds do not exist");
        }

        user.getDespensa().clear();
        user.getDespensa().addAll(ingredients);
        usuarioRepository.save(user);
        return ingredients.stream().map(ingredienteService::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<IngredienteResponseDto> get(String email) {
        Usuario user = usuarioRepository.findWithDespensaByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
        return user.getDespensa().stream()
                .sorted((a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()))
                .map(ingredienteService::toResponse)
                .toList();
    }
}
