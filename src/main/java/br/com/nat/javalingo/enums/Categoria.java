package br.com.nat.javalingo.enums;

public enum Categoria {
    NOVA (0,2),
    REVISAO (3,4),
    APRENDIDA (5,5);

    final int min;
    final int max;

    Categoria(int min, int max){
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

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
            case NOVA -> "Nova";
            case REVISAO -> "Revisão";
            case APRENDIDA -> "Aprendida";
        };
    }
}
