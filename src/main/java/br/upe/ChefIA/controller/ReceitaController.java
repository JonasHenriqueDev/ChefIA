package br.upe.ChefIA.controller;

import br.upe.ChefIA.Receita;
import br.upe.ChefIA.service.ReceitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receita")

@RequiredArgsConstructor
public class ReceitaController {

    private final ReceitaService receitaService;

    @GetMapping()
    public List<Receita> list() {
        return receitaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Receita> findById(@PathVariable("id") Long id) {
        Receita entity = receitaService.findById(id);
        return ResponseEntity.ok(entity);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public Receita add(@RequestBody Receita receita) {
        return receitaService.save(receita);
    }
}
