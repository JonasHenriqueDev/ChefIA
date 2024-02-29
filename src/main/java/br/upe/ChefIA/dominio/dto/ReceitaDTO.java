package br.upe.ChefIA.dominio.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReceitaDTO {

    private Long id;
    private String nome;
    private List<String> ingredientes;
    private List<String> macro_ingredientes;
    private List<String> modo_de_preparo;
}
