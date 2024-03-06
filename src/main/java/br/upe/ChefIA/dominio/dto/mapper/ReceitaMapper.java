package br.upe.ChefIA.dominio.dto.mapper;

import br.upe.ChefIA.dominio.Receita;
import br.upe.ChefIA.dominio.dto.ReceitaDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ReceitaMapper {
    private final ModelMapper mapper;

    public Receita toEntity(ReceitaDTO dto) {
        return mapper.map(dto, Receita.class);
    }

    public ReceitaDTO toDTO(Receita entity) {
        return mapper.map(entity, ReceitaDTO.class);
    }

    public List<ReceitaDTO> toDTO(List<Receita> receitas) {
        return receitas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

}
