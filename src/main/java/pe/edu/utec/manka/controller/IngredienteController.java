package pe.edu.utec.manka.controller;

import org.springframework.web.bind.annotation.*;
import pe.edu.utec.manka.dto.IngredienteResponseDto;
import pe.edu.utec.manka.service.IngredienteService;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredienteController {
    private final IngredienteService ingredienteService;

    public IngredienteController(IngredienteService ingredienteService) {
        this.ingredienteService = ingredienteService;
    }

    @GetMapping
    public List<IngredienteResponseDto> search(@RequestParam(required = false) String search) {
        return ingredienteService.search(search);
    }
}
