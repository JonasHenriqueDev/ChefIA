package br.upe.ChefIA.dominio;

import lombok.Data;
import jakarta.persistence.*;

import java.lang.reflect.Type;
import java.util.List;

@Data
@Entity(name = "receitas")
public class Receita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nome;

    @ElementCollection
    @Column(name = "ingrediente")
    private List<String> ingredientes;

    @ElementCollection
    @Column(name = "macro_ingrediente")
    private List<String> macro_ingredientes;

    @ElementCollection
    @Column
    @Lob
    private List<String> modo_de_preparo;
}
