package br.com.nat.javalingo.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.List;

public record DadosPalavra(
        @JsonAlias("original") String original,
        @JsonAlias("traducao") String traducao,
        @JsonAlias("exemplos") List<DadosTraducao> exemplos
        ){}