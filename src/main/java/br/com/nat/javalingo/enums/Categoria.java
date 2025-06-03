package br.com.nat.javalingo.enums;

public enum Categoria {
    NOVA,
    REVISAO,
    APRENDIDA;

    public static Categoria converterNivelParaCategoria(int nivelAprendizado){
        switch (nivelAprendizado){
            case 0, 1, 2:
                return NOVA;
            case 3, 4:
                return REVISAO;
            case 5:
                return APRENDIDA;
            default:
                throw new IllegalArgumentException("Valor inválido para categoria");
        }
    }

    @Override
    public String toString() {
        return switch (this){
            case NOVA -> "NOVA";
            case REVISAO -> "REVISÃO";
            case APRENDIDA -> "APRENDIDA";
        };
    }
}
