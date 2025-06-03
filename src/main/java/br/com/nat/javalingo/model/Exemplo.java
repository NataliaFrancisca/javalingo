package br.com.nat.javalingo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "exemplos")
public class Exemplo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String original;
    @Column(unique = true, nullable = false)
    private String traducao;

    @ManyToOne
    Palavra palavra;

    public Long getId() {
        return id;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getTraducao() {
        return traducao;
    }

    public void setTraducao(String traducao) {
        this.traducao = traducao;
    }

    public Palavra getPalavra() {
        return palavra;
    }

    public void setPalavra(Palavra palavra) {
        this.palavra = palavra;
    }

    @Override
    public String toString() {
        return "%s > %s".formatted(original, traducao);
    }
}
