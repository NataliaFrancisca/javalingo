package br.com.nat.javalingo.model;

import jakarta.persistence.*;

@Entity
@Table(
    name = "exemplos",
    uniqueConstraints = @UniqueConstraint(columnNames = {"original", "traducao"})
)
public class Exemplo extends Traducao{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    Palavra palavra;

    public Exemplo(){}

    public Exemplo(String original, String traducao, Palavra palavra){
        super(original, traducao);
        this.palavra = palavra;
    }

    public Long getId() {
        return id;
    }

    public Palavra getPalavra() {
        return palavra;
    }

    public void setPalavra(Palavra palavra) {
        this.palavra = palavra;
    }

    @Override
    public String toString() {
        return "%s > %s".formatted(this.getOriginal(), this.getTraducao());
    }
}
