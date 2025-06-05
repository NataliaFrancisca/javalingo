package br.com.nat.javalingo.principal;

import br.com.nat.javalingo.enums.Categoria;
import br.com.nat.javalingo.model.DadosPalavra;
import br.com.nat.javalingo.repository.PalavraRepository;

import br.com.nat.javalingo.service.PalavraServico;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

@Service
public class Principal {
    private final Scanner scanner = new Scanner(System.in);
    private final PalavraServico palavraServico;

    public Principal(PalavraRepository palavraRepository){
        this.palavraServico = new PalavraServico(palavraRepository);
    }

    public void exibirMenu(){
        System.out.println("\n*** JAVALINGO ***");
        System.out.println("seu sistema de tradução e armazenamento de palavras.");

        var usuarioMenuOpcao = -1;

        while(usuarioMenuOpcao != 9){
            String menu = """
                    \s
                    O que deseja fazer agora:
                    1. Traduzir Palavra
                    3. Listar todas as Palavras
                    4. Listar todos os Exemplos
                    5. Filtrar Palavras por Categoria
                    6. Buscar Palavra
                    \s
                    9. Encerrar.
                    \n""";

            System.out.println(menu);
            usuarioMenuOpcao = this.scanner.nextInt();
            this.scanner.nextLine();

            switch (usuarioMenuOpcao){
                case 1:
                    traduzirPalavra();
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

    public void traduzirPalavra(){
        System.out.println("Digite a palavra que você deseja traduzir:");
        var palavraParaTraducao = this.scanner.nextLine();

        List<DadosPalavra> palavrasTraduzidas = this.palavraServico.traduzirPalavra(palavraParaTraducao);

        System.out.println("** resultado da tradução (gerado por inteligência artificial.) **");
        palavrasTraduzidas.forEach(p -> {
            System.out.printf("""
                    %s = %s
                    
                    exemplos: %s
                    %n""", p.original(), p.traducao(), p.exemplos());
        });

        System.out.println("Deseja salvar essas traduções? (sim|nao) ");
        var usuarioDesejaSalvarTraducao = this.scanner.nextLine();

        if(!usuarioDesejaSalvarTraducao.equalsIgnoreCase("sim")){
            return;
        }

        System.out.println("""
            Em qual categoria você deseja inserir as traduções:
            
            1. Novas
            2. Revisão
            """
        );

        var usuarioEscolhaCategoria = this.scanner.nextInt();
        this.scanner.nextLine();

        if(usuarioEscolhaCategoria < 1 || usuarioEscolhaCategoria > 2){
            System.out.println("Opção digitada é inválida.");
            return;
        }

        Categoria categoria = usuarioEscolhaCategoria == 1 ? Categoria.NOVA : Categoria.REVISAO;
        this.palavraServico.salvarPalavras(palavrasTraduzidas, categoria);
    }

    public void listarTodasAsPalavras(){
        this.palavraServico.listarPalavras();
    }

    public void listarTodasAsFrases(){
        this.palavraServico.listarFrases();
    }

    public void filtrarPalavrasPorCategoria(){
        System.out.println("""
            Escolha a categoria para filtrar as palavras:
            
            1. Novas
            2. Revisão
            3. Aprendidas
            """
        );

        int usuarioEscolhaCategoria = this.scanner.nextInt();
        this.scanner.nextLine();

        Categoria categoria = Categoria.values()[usuarioEscolhaCategoria-1];
        this.palavraServico.filtrarPalavrasPorCategoria(categoria);
    }

    public void pesquisarPalavra(){
        System.out.println("Qual palavra você deseja procurar: ");
        var usuarioPesquisa = this.scanner.nextLine();
        this.palavraServico.buscarPalavra(usuarioPesquisa);
    }
}
