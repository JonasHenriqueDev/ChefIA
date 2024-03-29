package br.upe.ChefIA.repository;

import br.upe.ChefIA.dominio.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long> {

    @Query("SELECT r FROM receitas r JOIN r.macro_ingredientes i WHERE i IN :ingredientes GROUP BY r HAVING COUNT(DISTINCT i) = :totalIngredientes AND NOT EXISTS (SELECT 1 FROM r.macro_ingredientes i2 WHERE i2 IN :ingredientesSem)")
    List<Receita> findByIngredientesIn(@Param("ingredientes") List<String> ingredientes, @Param("ingredientesSem") List<String> ingredientesSem, int totalIngredientes);

    @Query("SELECT r FROM receitas r WHERE NOT EXISTS (SELECT 1 FROM r.macro_ingredientes i WHERE i IN :ingredientes)")
    List<Receita> findByNotIngredientesIn(@Param("ingredientes") List<String> ingredientes);

}
