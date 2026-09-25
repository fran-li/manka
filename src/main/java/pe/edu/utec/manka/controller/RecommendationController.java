package pe.edu.utec.manka.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pe.edu.utec.manka.dto.RecommendationRequestDto;
import pe.edu.utec.manka.dto.RecommendationResponseDto;
import pe.edu.utec.manka.service.RecommendationService;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping
    public List<RecommendationResponseDto> recommend(@AuthenticationPrincipal Jwt jwt,
                                                       @Valid @RequestBody RecommendationRequestDto request) {
        return recommendationService.recommend(jwt.getSubject(), request);
    }
}
