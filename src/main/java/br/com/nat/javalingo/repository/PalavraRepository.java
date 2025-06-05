package br.com.nat.javalingo.repository;

import br.com.nat.javalingo.model.Palavra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PalavraRepository extends JpaRepository<Palavra, Long> {
    @Query("SELECT p FROM Palavra p WHERE p.nivelAprendizado BETWEEN :minimo AND :maximo")
    List<Palavra> buscarPalavrasPorNivelAprendizado(int minimo, int maximo);

    @Query("SELECT p FROM Palavra p WHERE UPPER(p.original) ILIKE UPPER(%:traducao%) OR UPPER(p.traducao) ILIKE UPPER(%:original%)")
    Optional<Palavra> buscarPalavraPorAmbosCampos(String original, String traducao);

    @Query("SELECT p FROM Palavra p WHERE p.original ILIKE CONCAT('%', :palavra, '%') OR p.traducao ILIKE CONCAT('%', :palavra, '%')")
    List<Palavra> buscarPalavrasQueContenhamBusca(@Param("palavra") String palavra);
}
