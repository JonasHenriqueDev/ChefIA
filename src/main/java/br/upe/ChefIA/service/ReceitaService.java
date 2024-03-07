package br.upe.ChefIA.service;

import br.upe.ChefIA.dominio.Receita;
import br.upe.ChefIA.dominio.dto.IngredienteDTO;
import br.upe.ChefIA.dominio.dto.ReceitaDTO;
import br.upe.ChefIA.dominio.dto.mapper.ReceitaMapper;
import br.upe.ChefIA.helper.NullAwareBeanUtils;
import br.upe.ChefIA.repository.ReceitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceitaService {

    private static final String RECEITA_NOT_FOUND_MSG = "Infelizmente não sei nenhuma receita com estes ingredientes";
    private static final String SPACE_REGEX = "\\s+";
    private static final String OU_REGEX = "\\s+ou\\s+";

    private final ReceitaRepository receitaRepository;
    private final ReceitaMapper mapper;
    private final NullAwareBeanUtils beanUtilsBean;

    public List<Receita> findAll() {
        return receitaRepository.findAll();
    }

    public Receita findById(Long id) {
        return receitaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receita não encontrada"));
    }

    public void deleteById(Long id) {
        receitaRepository.deleteById(id);
    }

    public Receita save(ReceitaDTO dto) {
        Receita receita = mapper.toEntity(dto);
        return receitaRepository.save(receita);
    }

    public Receita update(Long id, ReceitaDTO dto) throws InvocationTargetException, IllegalAccessException {
        Receita currentReceita = receitaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receita não encontrada"));
        Receita updatedReceita = mapper.toEntity(dto);

        beanUtilsBean.copyProperties(currentReceita, updatedReceita);

        return receitaRepository.save(currentReceita);
    }

    public List<Receita> generate(IngredienteDTO dto) {
        List<Receita> generatedReceitas = new ArrayList<>();
        String ingredientesString = dto.getIngredientesString();
        List<String> ingredientesList = new ArrayList<>();
        List<String> ingredientesExcluidos = new ArrayList<>();

        String[] partes = ingredientesString.split(OU_REGEX);
        for (String parte : partes) {

            String[] subPartes = parte.split(SPACE_REGEX);
            boolean excluir = false;
            for (String subParte : subPartes) {
                if (subParte.equals("sem")) {
                    excluir = true;
                } else if (excluir) {
                    ingredientesExcluidos.add(subParte.trim());
                } else {
                    ingredientesList.add(subParte.trim());
                }
            }

            generatedReceitas.addAll(findByIngredientes(ingredientesList, ingredientesExcluidos));
        }

        Collections.shuffle(generatedReceitas);
        if (generatedReceitas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, RECEITA_NOT_FOUND_MSG);
        }
        if (generatedReceitas.size() > 3) {
            generatedReceitas = generatedReceitas.subList(0, 3);
        }

        return generatedReceitas;
    }

    private List<Receita> findByIngredientes(List<String> ingredientesList, List<String> ingredientesExcluidos) {
        ingredientesList.removeAll(Collections.singleton("e"));
        int totalIngredientes = ingredientesList.size();

        System.out.println(ingredientesExcluidos.isEmpty());
        if (ingredientesList.isEmpty()) {
            return receitaRepository.findByNotIngredientesIn(ingredientesExcluidos);
        }

        System.out.println("sem: " + ingredientesExcluidos);
        System.out.println("list: " + ingredientesList);

        return receitaRepository.findByIngredientesIn(ingredientesList, ingredientesExcluidos, totalIngredientes);
    }
}
