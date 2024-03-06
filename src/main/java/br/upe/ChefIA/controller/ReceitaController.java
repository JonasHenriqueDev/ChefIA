package br.upe.ChefIA.controller;

import br.upe.ChefIA.dominio.Receita;
import br.upe.ChefIA.dominio.dto.IngredienteDTO;
import br.upe.ChefIA.dominio.dto.ReceitaDTO;
import br.upe.ChefIA.service.ReceitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
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
    public Receita add(@RequestBody ReceitaDTO dto) {
        return receitaService.save(dto);
    }

    @PatchMapping("/{id}")
    public Receita editById(@PathVariable("id") Long id, @RequestBody ReceitaDTO dto) throws InvocationTargetException, IllegalAccessException {
        return receitaService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") Long id) {
        receitaService.deleteById(id);
    }

    @PostMapping("/gerar")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Receita>> generateReceita(@RequestBody IngredienteDTO ingredientes) {
        List<Receita> receitas = receitaService.generate(ingredientes);

        if (receitas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(receitas);
    }
}
