package br.com.nat.javalingo.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class Traducao {
    @Column(nullable = false)
    private String original;
    @Column(nullable = false)
    private String traducao;

    public Traducao(){}

    public Traducao(String original, String traducao){
        this.original = original;
        this.traducao = traducao;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getOriginal() {
        return original;
    }

    public void setTraducao(String traducao) {
        this.traducao = traducao;
    }

    public String getTraducao() {
        return traducao;
    }
}
