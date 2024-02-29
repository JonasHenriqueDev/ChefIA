package br.upe.ChefIA.service;

import br.upe.ChefIA.dominio.Receita;
import br.upe.ChefIA.dominio.dto.IngredienteDTO;
import br.upe.ChefIA.repository.ReceitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceitaService {

    private List<Receita> generatedReceitas;
    private List<String> ingredientesList;
    private String ingredientesString;

    public final ReceitaRepository receitaRepository;

    public List<Receita> findAll() {
        return receitaRepository.findAll();
    }

    public Receita findById(Long id) {
        return receitaRepository.findById(id).get();
    }

    public Receita save(Receita receita) {
        return receitaRepository.save(receita);
    }

    public List<String> generate(IngredienteDTO dto) {
        generatedReceitas = new ArrayList<Receita>();

        ingredientesString = dto.getIngredientesString();
        ingredientesString = ingredientesString.replaceAll("\\s+e\\s+", " ");
        ingredientesList = List.of(ingredientesString.split("\\s+"));


        return ingredientesList;
    }

}
