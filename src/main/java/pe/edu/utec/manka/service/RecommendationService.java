package pe.edu.utec.manka.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utec.manka.dto.RecommendationBreakdownDto;
import pe.edu.utec.manka.dto.RecommendationRequestDto;
import pe.edu.utec.manka.dto.RecommendationResponseDto;
import pe.edu.utec.manka.entity.HistorialCocina;
import pe.edu.utec.manka.entity.Ingrediente;
import pe.edu.utec.manka.entity.Plato;
import pe.edu.utec.manka.entity.Usuario;
import pe.edu.utec.manka.exception.BusinessRuleException;
import pe.edu.utec.manka.exception.ResourceNotFoundException;
import pe.edu.utec.manka.recommendation.RecommendationContext;
import pe.edu.utec.manka.recommendation.RecommendationEngine;
import pe.edu.utec.manka.recommendation.RecommendationResult;
import pe.edu.utec.manka.repository.HistorialCocinaRepository;
import pe.edu.utec.manka.repository.IngredienteRepository;
import pe.edu.utec.manka.repository.PlatoRepository;
import pe.edu.utec.manka.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RecommendationService {
    private final UsuarioRepository usuarioRepository;
    private final IngredienteRepository ingredienteRepository;
    private final PlatoRepository platoRepository;
    private final HistorialCocinaRepository historialRepository;
    private final RecommendationEngine recommendationEngine;

    public RecommendationService(UsuarioRepository usuarioRepository,
                                 IngredienteRepository ingredienteRepository,
                                 PlatoRepository platoRepository,
                                 HistorialCocinaRepository historialRepository,
                                 RecommendationEngine recommendationEngine) {
        this.usuarioRepository = usuarioRepository;
        this.ingredienteRepository = ingredienteRepository;
        this.platoRepository = platoRepository;
        this.historialRepository = historialRepository;
        this.recommendationEngine = recommendationEngine;
    }

    @Transactional(readOnly = true)
    public List<RecommendationResponseDto> recommend(String email, RecommendationRequestDto request) {
        Usuario user = usuarioRepository.findWithDespensaByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        Set<Ingrediente> availableIngredients = resolveIngredients(request, user);
        if (availableIngredients.isEmpty()) {
            throw new BusinessRuleException("Add at least one ingredient or save ingredients in your pantry first");
        }

        List<Plato> candidates = platoRepository.findCandidates(request.getAvailableMinutes());
        List<HistorialCocina> recentHistory = historialRepository.findRecentByUserId(
                user.getId(), LocalDateTime.now().minusDays(30));

        RecommendationContext context = new RecommendationContext(
                availableIngredients,
                request.getAvailableMinutes(),
                recentHistory
        );

        int limit = request.getLimit() == null ? 10 : request.getLimit();
        return recommendationEngine.rank(candidates, context, limit).stream()
                .map(result -> toResponse(result, request.getAvailableMinutes()))
                .toList();
    }

    private Set<Ingrediente> resolveIngredients(RecommendationRequestDto request, Usuario user) {
        if (request.getIngredientIds() == null || request.getIngredientIds().isEmpty()) {
            return new HashSet<>(user.getDespensa());
        }

        List<Ingrediente> ingredients = ingredienteRepository.findAllById(request.getIngredientIds());
        if (ingredients.size() != request.getIngredientIds().size()) {
            throw new BusinessRuleException("One or more ingredientIds do not exist");
        }
        return new HashSet<>(ingredients);
    }

    private RecommendationResponseDto toResponse(RecommendationResult result, int availableMinutes) {
        RecommendationResponseDto response = new RecommendationResponseDto();
        Plato dish = result.getPlato();
        response.setPlatoId(dish.getId());
        response.setNombre(dish.getNombre());
        response.setTiempoPreparacion(dish.getTiempoPreparacion());
        response.setDificultad(dish.getDificultad());
        response.setScore(result.getScoring().getFinalScore());
        response.setIngredientCoveragePercent(result.getCoveragePercent());
        response.setMatchedIngredients(result.getMatchedIngredients());
        response.setTotalIngredients(result.getTotalIngredients());
        response.setMissingIngredients(result.getMissingIngredients());
        response.setBreakdown(toBreakdown(result.getScoring().getBreakdown()));
        response.setReasons(buildReasons(result, availableMinutes));
        return response;
    }

    private RecommendationBreakdownDto toBreakdown(Map<String, Double> scores) {
        RecommendationBreakdownDto dto = new RecommendationBreakdownDto();
        dto.setIngredientCoverage(scores.getOrDefault("ingredientCoverage", 0.0));
        dto.setTime(scores.getOrDefault("time", 0.0));
        dto.setProteinVariety(scores.getOrDefault("proteinVariety", 0.0));
        dto.setRepetition(scores.getOrDefault("repetition", 0.0));
        dto.setPopularity(scores.getOrDefault("popularity", 0.0));
        return dto;
    }

    private List<String> buildReasons(RecommendationResult result, int availableMinutes) {
        List<String> reasons = new ArrayList<>();
        reasons.add("Usas " + result.getMatchedIngredients() + " de " + result.getTotalIngredients() + " ingredientes necesarios");
        reasons.add("Listo en " + result.getPlato().getTiempoPreparacion() + " min; entra en tus " + availableMinutes + " min disponibles");

        if (result.getMissingIngredients().isEmpty()) {
            reasons.add("Tienes el 100% de los ingredientes necesarios");
        } else {
            reasons.add("Tienes el " + result.getCoveragePercent() + "% de lo necesario; faltan " + result.getMissingIngredients().size() + " ingredientes");
        }

        double repetition = result.getScoring().getBreakdown().getOrDefault("repetition", 100.0);
        if (repetition == 0.0) {
            reasons.add("Lo cocinaste en los últimos 7 días, por eso recibe penalización por repetición");
        } else {
            reasons.add("No lo cocinaste en los últimos 7 días");
        }
        return reasons;
    }
}
