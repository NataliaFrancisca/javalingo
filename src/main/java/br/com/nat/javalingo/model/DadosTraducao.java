package br.com.nat.javalingo.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record DadosTraducao(
        @JsonAlias("original") String original,
        @JsonAlias("traducao") String traducao
) {
}
