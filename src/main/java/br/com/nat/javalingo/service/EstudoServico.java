package br.com.nat.javalingo.service;

import br.com.nat.javalingo.model.Exemplo;
import br.com.nat.javalingo.model.Palavra;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class EstudoServico {
    private int numeroQuestoesCorretas = 0;
    private final Random random = new Random();
    private final Scanner scanner = new Scanner(System.in);

    private void verificarRespostaTraducao(Palavra palavra, String respostaTraducaoUsuario){
        String respostaNormalizada = respostaTraducaoUsuario.trim().toLowerCase();
        String traducaoNormalizada = palavra.getTraducao().trim().toLowerCase();

        if (respostaNormalizada.equals(traducaoNormalizada)) {
            System.out.println("parabéns, você acertou! +1 no seu conhecimento.");
            palavra.setNivelAprendizado();
            this.numeroQuestoesCorretas++;
        } else {
            System.out.println("ops! tradução incorreta.");
            System.out.println("tradução correta: " + palavra.getTraducao());
        }
    }

    private void exibirResultadoFinal(int numeroTraducoesCorretas, int numeroTraducoes){
        System.out.printf("""
                
                Resultado Final:
                
                Número de traduções corretas: %s
                Número de questões incorretas: %s
                %n""", numeroTraducoesCorretas, numeroTraducoes - numeroTraducoesCorretas);
    }

    public void realizarEstudo(List<Palavra> palavras){
        for (Palavra palavra : palavras) {
            List<Exemplo> exemplos = palavra.getExemplos();
            Exemplo exemplo = exemplos.get(random.nextInt(exemplos.size()));

            System.out.println("\n");
            System.out.println("***".repeat(6));
            System.out.printf("Exemplo aplicado em uma frase: '%s'.", exemplo.getOriginal());
            System.out.printf("\nTraduza a palavra (%s): ", palavra.getOriginal());

            var respostaTraducaoUsuario = this.scanner.nextLine();
            this.verificarRespostaTraducao(palavra, respostaTraducaoUsuario);
            System.out.println("***".repeat(6));
        }

        this.exibirResultadoFinal(numeroQuestoesCorretas, palavras.size());
    }
}
