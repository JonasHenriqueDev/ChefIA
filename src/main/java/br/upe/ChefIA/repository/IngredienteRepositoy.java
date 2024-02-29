package br.upe.ChefIA.repository;

import br.upe.ChefIA.dominio.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredienteRepositoy extends JpaRepository<Ingrediente, Long> {
}
