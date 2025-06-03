package br.com.nat.javalingo.repository;

import br.com.nat.javalingo.model.Palavra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PalavraRepository extends JpaRepository<Palavra, Long> {
    @Query("SELECT p FROM Palavra p WHERE p.nivelAprendizado BETWEEN :minimo AND :maximo")
    List<Palavra> buscarPalavrasPorNivelAprendizado(int minimo, int maximo);

    @Query("SELECT p FROM Palavra p WHERE UPPER(p.original) ILIKE UPPER(%:usuarioPesquisa%) OR UPPER(p.traducao) ILIKE UPPER(%:usuarioPesquisa%)")
    List<Palavra> buscarPalavras(String usuarioPesquisa);
}
