package br.upe.ChefIA.service;

import br.upe.ChefIA.dominio.Receita;
import br.upe.ChefIA.dominio.dto.IngredienteDTO;
import br.upe.ChefIA.dominio.dto.ReceitaDTO;
import br.upe.ChefIA.dominio.dto.mapper.ReceitaMapper;
import br.upe.ChefIA.repository.ReceitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ReceitaService {

    private final ReceitaRepository receitaRepository;
    private final ReceitaMapper mapper;

    public List<Receita> findAll() {
        return receitaRepository.findAll();
    }

    public Receita findById(Long id) {
        return receitaRepository.findById(id).get();
    }

    public void deleteById(Long id) {
        receitaRepository.deleteById(id);
    }

    public Receita save(ReceitaDTO dto) {
        Receita receita = mapper.toEntity(dto);
        return receitaRepository.save(receita);
    }

    public Receita update(Long id, ReceitaDTO dto) {
        Receita current = this.findById(id);
        Receita updated = mapper.toEntity(dto);

        BeanUtils.copyProperties(updated, current, "id");

        ReceitaDTO currentDTO = mapper.toDTO(current);

        return this.save(currentDTO);
    }

    public List<Receita> generate(IngredienteDTO dto) {
        List<Receita> generatedReceitas;
        List<String> ingredientesList;
        String ingredientesString;

        ingredientesString = dto.getIngredientesString();
        ingredientesString = ingredientesString.replaceAll("\\s+e\\s+", " ");
        ingredientesList = new ArrayList<>(Arrays.asList(ingredientesString.split("\\s+")));

        generatedReceitas = receitaRepository.findByIngredientesIn(ingredientesList);

        Collections.shuffle(generatedReceitas);

        if (generatedReceitas.size() > 3) {
            generatedReceitas = generatedReceitas.subList(0, 3);
        }

        return generatedReceitas;
    }

}
