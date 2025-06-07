package br.com.nat.javalingo.principal;

import br.com.nat.javalingo.enums.Categoria;
import br.com.nat.javalingo.model.DadosPalavra;
import br.com.nat.javalingo.model.Palavra;
import br.com.nat.javalingo.repository.PalavraRepository;

import br.com.nat.javalingo.service.EstudoServico;
import br.com.nat.javalingo.service.PalavraServico;
import br.com.nat.javalingo.service.TraducaoServico;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Scanner;

@Service
public class Principal {
    private final Scanner scanner = new Scanner(System.in);
    private final PalavraServico palavraServico;
    private final EstudoServico estudoServico;
    private final TraducaoServico traducaoServico;

    public Principal(PalavraRepository palavraRepository){
        this.palavraServico = new PalavraServico(palavraRepository);
        this.estudoServico = new EstudoServico();
        this.traducaoServico = new TraducaoServico();
    }

    private Categoria exibirMenuCategoriaERetornaCategoria(){

        System.out.println("""
            Escolha uma categoria:
            
            1. Novas
            2. Revisão
            3. Aprendidas
            """
        );

        try{
            int usuarioEscolhaCategoria = Integer.parseInt(scanner.nextLine());

            switch (usuarioEscolhaCategoria){
                case 1:
                    return Categoria.NOVA;
                case 2:
                    return Categoria.REVISAO;
                case 3:
                    return Categoria.APRENDIDA;
                default:
                    System.out.println("Opção escolhida é inválida.");
            }

        }catch (NumberFormatException exception){
            System.out.println("Entrada inválida, digite um número.");
        }

        return null;
    }

    public void exibirMenu(){
        System.out.println("\n*** JAVALINGO ***");
        System.out.println("seu sistema de tradução e armazenamento de palavras.");

        var usuarioMenuOpcao = -1;

        while(usuarioMenuOpcao != 9){
            String menu = """
                    \s
                    O que deseja fazer agora:
                    1. Traduzir Palavra.
                    2. Listar Todas as Palavras.
                    3. Listar Todos os Exemplos.
                    4. Filtrar Palavras por Categoria.
                    5. Buscar Palavra.
                    6. Estudar Palavras.
                    \s
                    9. Encerrar.
                    """;

            System.out.println(menu);
            usuarioMenuOpcao = this.scanner.nextInt();
            this.scanner.nextLine();

            switch (usuarioMenuOpcao){
                case 1:
                    traduzirPalavra();
                    break;
                case 2:
                    listarTodasAsPalavras();
                    break;
                case 3:
                    listarTodasAsFrases();
                    break;
                case 4:
                    listarPalavrasPorCategoria();
                    break;
                case 5:
                    pesquisarPalavra();
                    break;
                case 6:
                    estudarPalavras();
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
        System.out.print("Digite a palavra que você deseja traduzir: ");
        var palavraParaTraducao = this.scanner.nextLine();

        List<DadosPalavra> palavrasTraduzidas = this.traducaoServico.traduzir(palavraParaTraducao);

        System.out.println("Deseja salvar essas traduções? (sim|nao) ");
        var usuarioDesejaSalvarTraducao = this.scanner.nextLine();

        if(!usuarioDesejaSalvarTraducao.equalsIgnoreCase("sim")){
            return;
        }

        Categoria categoria = this.exibirMenuCategoriaERetornaCategoria();

        if(categoria == null){
            return;
        }

        this.palavraServico.salvarPalavras(palavrasTraduzidas, categoria);
    }

    public void listarTodasAsPalavras(){
        this.palavraServico.listarPalavras();
    }

    public void listarTodasAsFrases(){
        this.palavraServico.listarFrases();
    }

    public void pesquisarPalavra(){
        System.out.println("Qual palavra você deseja pesquisar: ");
        var usuarioPesquisa = this.scanner.nextLine();
        this.palavraServico.buscarPalavra(usuarioPesquisa);
    }

    public void listarPalavrasPorCategoria(){
        Categoria categoria = this.exibirMenuCategoriaERetornaCategoria();

        if(categoria == null){
            return;
        }

        this.palavraServico.filtrarEListarPalavrasPorCategoria(categoria);
    }

    public void estudarPalavras(){
        Categoria categoria = this.exibirMenuCategoriaERetornaCategoria();

        if(categoria == null){
            return;
        }

        List<Palavra> palavras = this.palavraServico.filtrarPalavrasPorCategoria(categoria);

        if(palavras.isEmpty()){
            System.out.println("Nenhuma palavra foi encontrada nessa categoria.");
            return;
        }

        this.estudoServico.realizarEstudo(palavras);
        this.palavraServico.atualizarNivelAprendizadoPalavras(palavras);

        System.out.println("Estudo concluído e níveis atualizados com sucesso.");
    }
}
