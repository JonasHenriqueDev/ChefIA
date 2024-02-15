package br.upe.ChefIA;

import lombok.Data;
import jakarta.persistence.*;
import java.util.List;

@Data
@Entity(name = "receitas")
public class Receita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @Column(name = "ingrediente")
    private List<String> ingredientes;

    @ElementCollection
    @Column(name = "macro_ingrediente")
    private List<String> macro_ingredientes;

    @Column(name = "modo_de_preparo")
    private List<String> modo_de_preparo;
}