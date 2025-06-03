package br.com.nat.javalingo.principal;

import br.com.nat.javalingo.dto.PalavraDTO;
import br.com.nat.javalingo.enums.Categoria;
import br.com.nat.javalingo.model.Exemplo;
import br.com.nat.javalingo.model.Palavra;
import br.com.nat.javalingo.repository.PalavraRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class Principal {
    private final Scanner scanner = new Scanner(System.in);
    private final PalavraRepository palavraRepository;

    public Principal(PalavraRepository palavraRepository){
        this.palavraRepository = palavraRepository;
    }

    public void exibirMenu(){
        System.out.println("*** JAVALINGO ***");
        System.out.println("seu sistema de tradução e armazenamento de palavras.");

        var usuarioMenuOpcao = -1;

        while(usuarioMenuOpcao != 9){
            String menu = """
                    \s
                    O que deseja fazer agora:
                    1. Traduzir de Português para Inglês.
                    3. Listar todas as Palavras.
                    4. Listar todos os Exemplos.
                    5. Filtrar Palavras por Categoria.
                    6. Buscar Palavra.
                    \s
                    9. Encerrar.
                    """;

            System.out.println(menu);
            usuarioMenuOpcao = this.scanner.nextInt();
            this.scanner.nextLine();

            switch (usuarioMenuOpcao){
                case 1:
                    traduzirParaPortugues();
                    break;
                case 3:
                    listarTodasAsPalavras();
                    break;
                case 4:
                    listarTodasAsFrases();
                    break;
                case 5:
                    filtrarPalavrasPorCategoria();
                    break;
                case 6:
                    pesquisarPalavra();
                    break;
                case 9:
                    System.out.println("Encerrando a aplicação.");
                    break;
                default:
                    System.out.println("Nenhuma opção válida.");
            }
        }
    }

    public void traduzirParaPortugues(){
        System.out.println("Digite a palavra que você deseja traduzir: (pt-br)");
        var palavraParaTraducao = this.scanner.nextLine();

        Palavra palavra = new Palavra();
        palavra.setOriginal("love");
        palavra.setTraducao("amor");
        palavra.setNivelAprendizado(5);

        List<Exemplo> exemplos = new ArrayList<>();
        Exemplo exemplo = new Exemplo();
        exemplo.setOriginal("They are in love.");
        exemplo.setTraducao("Eles estão apaixonados.");
        exemplo.setPalavra(palavra);

        exemplos.add(exemplo);
        palavra.setExemplos(exemplos);
        this.palavraRepository.save(palavra);

        Palavra palavra1 = new Palavra();
        palavra1.setOriginal("love");
        palavra1.setTraducao("amar");
        palavra1.setNivelAprendizado(1);

        List<Exemplo> exemplos1 = new ArrayList<>();
        Exemplo exemplo1 = new Exemplo();
        exemplo1.setOriginal("She loves her job.");
        exemplo1.setTraducao("Ela ama o trabalho dela.");
        exemplo1.setPalavra(palavra1);

        exemplos1.add(exemplo1);
        palavra1.setExemplos(exemplos1);
        this.palavraRepository.save(palavra1);

        System.out.println(palavra);
        System.out.println(palavra1);
    }

    public void listarTodasAsPalavras(){
        List<Palavra> palavras = this.palavraRepository.findAll();

        if(palavras.isEmpty()){
            System.out.println("nenhuma palavra foi encontrada.");
            return;
        }

        System.out.println("** palavras encontradas **");
        palavras.forEach(p -> System.out.printf("%s = %s%n", p.getOriginal(), p.getTraducao()));
    }

    public void listarTodasAsFrases(){
        List<Palavra> palavras = this.palavraRepository.findAll();

        if(palavras.isEmpty()){
            System.out.println("nenhuma palavra foi encontrada.");
            return;
        }

        List<Exemplo> exemplos = palavras.stream().flatMap(p -> p.getExemplos().stream()).toList();

        System.out.println("** todas as frases encontradas **");
        for (int i = 0; i < exemplos.size() ; i++) {
            Exemplo e = exemplos.get(i);
            System.out.println(i+1 + ". " + e.getOriginal() + " = " + e.getTraducao());
        }
    }

    public void filtrarPalavrasPorCategoria(){
        System.out.println("Qual categoria você deseja filtrar as palavras: ");
        System.out.println("""
                1. Nova
                2. Revisao
                3. Aprendida
                """);

        int usuarioOpcao = this.scanner.nextInt();
        this.scanner.nextLine();

        Categoria categoria = Categoria.values()[usuarioOpcao-1];
        List<Palavra> palavras = this.palavraRepository.buscarPalavrasPorNivelAprendizado(categoria.getMin(), categoria.getMax());

        if(palavras.isEmpty()){
            System.out.println("nenhuma palavra foi encontrada nessa categoria.");
            return;
        }

        System.out.println("** palavras da categoria **");
        palavras.forEach(p -> System.out.printf("%s = %s%n", p.getOriginal(), p.getTraducao()));
    }

    public void pesquisarPalavra(){
        System.out.println("Qual palavra você deseja procurar: ");
        var usuarioPesquisa = this.scanner.nextLine();

        List<Palavra> palavras = this.palavraRepository.buscarPalavras(usuarioPesquisa);

        if(palavras.isEmpty()){
            System.out.println("nenhuma palavra foi encontrada.");
            return;
        }

        System.out.println("** palavras encontradas **");
        palavras.stream().map(PalavraDTO::criarDTO).forEach(System.out::println);
    }
}
