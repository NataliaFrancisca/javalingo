package br.com.nat.javalingo.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "palavras",
    uniqueConstraints = @UniqueConstraint(columnNames = {"original", "traducao"})
)
public class Palavra extends Traducao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int nivelAprendizado = 0;
    private final LocalDate dataAdicao = LocalDate.now();

    @OneToMany(mappedBy = "palavra", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Exemplo> exemplos = new ArrayList<>();

    public Palavra(){}

    public Palavra(DadosPalavra dadosPalavra, int nivelAprendizado){
        super(dadosPalavra.original(), dadosPalavra.traducao());
        this.nivelAprendizado = nivelAprendizado;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDataAdicao() {
        return dataAdicao;
    }

    public List<Exemplo> getExemplos() {
        return exemplos;
    }

    public void setExemplos(List<Exemplo> exemplos) {
        this.exemplos = exemplos;
    }

    public int getNivelAprendizado() {
        return nivelAprendizado;
    }

    public void setNivelAprendizado(int nivelAprendizado) {
        if(nivelAprendizado < 0 || nivelAprendizado > 5){
            throw new IllegalArgumentException("Nível de aprendizado deve ser entre 0 e 5");
        }

        this.nivelAprendizado = nivelAprendizado;
    }
}
