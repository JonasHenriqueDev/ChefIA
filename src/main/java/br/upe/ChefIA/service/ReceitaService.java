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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class ReceitaService {

    private static final String RECEITA_NOT_FOUND_MSG = "Infelizmente não sei nenhuma receita com estes ingredientes";
    private static final String SPACE_REGEX = "\\s+";
    private static final String OU_REGEX = "\\s+ou\\s+";

    private static final String LOG_BUSCANDO_TODAS_RECEITAS = "Buscando todas as receitas.";
    private static final String LOG_BUSCANDO_RECEITA_POR_ID = "Buscando receita pelo ID: {}";
    private static final String LOG_DELETANDO_RECEITA_POR_ID = "Deletando receita pelo ID: {}";
    private static final String LOG_SALVANDO_NOVA_RECEITA = "Salvando nova receita.";
    private static final String LOG_ATUALIZANDO_RECEITA_POR_ID = "Atualizando receita pelo ID: {}";
    private static final String LOG_GERANDO_RECEITAS = "Gerando receitas com base nos ingredientes fornecidos.";
    private static final String LOG_NENHUMA_RECEITA_ENCONTRADA = "Nenhuma receita encontrada com os ingredientes fornecidos.";
    private static final String LOG_BUSCANDO_RECEITAS_POR_INGREDIENTES = "Buscando receitas pelos ingredientes fornecidos.";
    private static final String LOG_VERIFICANDO_LISTA_INGREDIENTES_VAZIA = "Verificando se a lista de ingredientes é vazia: {}";

    // Logger
    private static final Logger logger = LoggerFactory.getLogger(ReceitaService.class);

    private final ReceitaRepository receitaRepository;
    private final ReceitaMapper mapper;
    private final NullAwareBeanUtils beanUtilsBean;

    public List<Receita> findAll() {
        logger.info(LOG_BUSCANDO_TODAS_RECEITAS);
        return receitaRepository.findAll();
    }

    public Receita findById(Long id) {
        logger.info(LOG_BUSCANDO_RECEITA_POR_ID, id);
        return receitaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receita não encontrada"));
    }

    public void deleteById(Long id) {
        logger.info(LOG_DELETANDO_RECEITA_POR_ID, id);
        receitaRepository.deleteById(id);
    }

    public Receita save(ReceitaDTO dto) {
        logger.info(LOG_SALVANDO_NOVA_RECEITA);
        Receita receita = mapper.toEntity(dto);
        return receitaRepository.save(receita);
    }

    public Receita update(Long id, ReceitaDTO dto) throws InvocationTargetException, IllegalAccessException {
        logger.info(LOG_ATUALIZANDO_RECEITA_POR_ID, id);
        Receita currentReceita = receitaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receita não encontrada"));
        Receita updatedReceita = mapper.toEntity(dto);

        beanUtilsBean.copyProperties(currentReceita, updatedReceita);

        return receitaRepository.save(currentReceita);
    }

    public List<Receita> generate(IngredienteDTO dto) {
        logger.info(LOG_GERANDO_RECEITAS);
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
            logger.warn(LOG_NENHUMA_RECEITA_ENCONTRADA);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, RECEITA_NOT_FOUND_MSG);
        }
        if (generatedReceitas.size() > 3) {
            generatedReceitas = generatedReceitas.subList(0, 3);
        }

        return generatedReceitas;
    }

    private List<Receita> findByIngredientes(List<String> ingredientesList, List<String> ingredientesExcluidos) {
        logger.info(LOG_BUSCANDO_RECEITAS_POR_INGREDIENTES);
        ingredientesList.removeAll(Collections.singleton("e"));
        int totalIngredientes = ingredientesList.size();

        logger.info(LOG_VERIFICANDO_LISTA_INGREDIENTES_VAZIA, ingredientesExcluidos.isEmpty());
        if (ingredientesList.isEmpty()) {
            return receitaRepository.findByNotIngredientesIn(ingredientesExcluidos);
        }

        return receitaRepository.findByIngredientesIn(ingredientesList, ingredientesExcluidos, totalIngredientes);
    }
}
