package br.com.nat.javalingo.dto;

import br.com.nat.javalingo.enums.Categoria;
import br.com.nat.javalingo.model.Exemplo;
import br.com.nat.javalingo.model.Palavra;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PalavraDTO {
    private String original;
    private String traducao;
    private Categoria categoria;
    private LocalDate dataAdicao;
    private List<Exemplo> exemplos;

    public static PalavraDTO criarDTO(Palavra palavra){
        PalavraDTO palavraDTO = new PalavraDTO();

        palavraDTO.original = palavra.getOriginal();
        palavraDTO.traducao = palavra.getTraducao();
        palavraDTO.categoria = Categoria.converterNivelParaCategoria(palavra.getNivelAprendizado());
        palavraDTO.dataAdicao = palavra.getDataAdicao();
        palavraDTO.exemplos = palavra.getExemplos();

        return palavraDTO;
    }

    @Override
    public String toString() {
        String exemplosString = exemplos.stream()
                .map(e -> "- " + e)
                .collect(Collectors.joining("\n"));

        return """
                Original: %s,
                Tradução: %s,
                Status: %s,
                Data Adição: %s,
                Exemplos:
                %s
                """.formatted(original.toUpperCase(), traducao.toUpperCase(), categoria, dataAdicao, exemplosString);
    }
}
