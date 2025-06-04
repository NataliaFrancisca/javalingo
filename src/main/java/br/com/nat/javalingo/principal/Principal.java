package br.com.nat.javalingo.principal;

import br.com.nat.javalingo.dto.PalavraDTO;
import br.com.nat.javalingo.enums.Categoria;
import br.com.nat.javalingo.model.DadosPalavra;
import br.com.nat.javalingo.model.Exemplo;
import br.com.nat.javalingo.model.Palavra;
import br.com.nat.javalingo.repository.PalavraRepository;

import br.com.nat.javalingo.service.GeminiAPI;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
                    1. Traduzir Palavra.
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

    private Palavra instanciarClassePalavra(DadosPalavra dadosPalavra, Categoria categoria){
        Palavra palavra = new Palavra();
        palavra.setOriginal(dadosPalavra.original());
        palavra.setTraducao(dadosPalavra.traducao());
        palavra.setNivelAprendizado(categoria.getMin());

        List<Exemplo> exemplos = dadosPalavra.exemplos().stream().map(e -> {
            Exemplo exemplo = new Exemplo();
            exemplo.setOriginal(e.original());
            exemplo.setTraducao(e.traducao());
            exemplo.setPalavra(palavra);
            return exemplo;
        }).toList();

        palavra.setExemplos(exemplos);
        return palavra;
    }

    private boolean verificarSeTraducaoJaExisteEmAmbosCampos(String original, String traducao){
        Optional<Palavra> palavra = this.palavraRepository.buscarPalavraPorAmbosCampos(original, traducao);
        return palavra.isEmpty();
    }

    private List<DadosPalavra> realizarTraducao(String palavraParaTraducao){
        GeminiAPI geminiAPI = new GeminiAPI();
        return geminiAPI.buscarDadosTraducao(palavraParaTraducao);
    }

    private void adicionarNovosExemplos(Palavra palavra, List<Exemplo> exemplos){
        List<Exemplo> exemplosAtualizados = exemplos.stream().peek(p -> p.setPalavra(palavra)).toList();
        palavra.setExemplos(exemplosAtualizados);
        this.palavraRepository.saveAndFlush(palavra);
    }

    private void salvarPalavrasNoBancoDeDados(List<Palavra> palavras){
        palavras.forEach(p -> {
            Optional<Palavra> traducaoJaCadastrada = this.palavraRepository.findPalavraByTraducaoIgnoreCase(p.getTraducao());

            if(traducaoJaCadastrada.isPresent()){
                Palavra palavra = traducaoJaCadastrada.get();
                this.adicionarNovosExemplos(palavra, p.getExemplos());
                System.out.println("adicionamos novos exemplos à palavra.");
            }else{
                boolean traducaoJaExiste = this.verificarSeTraducaoJaExisteEmAmbosCampos(p.getOriginal(), p.getTraducao());

                if(!traducaoJaExiste){
                    System.out.println("essa tradução já existe, mas em campos invertidos.");
                    return;
                }

                this.palavraRepository.save(p);
                System.out.println("palavra salva com sucesso...");
            }
        });
    }

    public void traduzirPalavra(){
        System.out.println("Digite a palavra que você deseja traduzir:");
        var palavraParaTraducao = this.scanner.nextLine();

        List<DadosPalavra> palavrasTraduzidas = this.realizarTraducao(palavraParaTraducao);

        System.out.println(" ** resultado da tradução (gerado por inteligência artificial.) **");
        palavrasTraduzidas.forEach(p -> {
            System.out.printf("""
                    %s = %s
                    
                    exemplos: %s
                    %n""", p.original(), p.traducao(), p.exemplos());
        });

        System.out.println("Deseja salvar essas traduções? (Sim/Nao) ");
        var usuarioDesejaSalvarTraducoesOpcao = this.scanner.nextLine();

        if(usuarioDesejaSalvarTraducoesOpcao.equalsIgnoreCase("sim")){
            System.out.println("""
                    Em qual categoria você deseja inserir as traduções:
                    1. Nova
                    2. Revisão
                    """);

            var usuarioCategoriaOpcao = this.scanner.nextInt();
            this.scanner.nextLine();

            if(usuarioCategoriaOpcao > 2 || usuarioCategoriaOpcao < 1){
                System.out.println("Opção inválida.");
                return;
            }

            Categoria categoria = usuarioCategoriaOpcao == 1 ? Categoria.NOVA : Categoria.REVISAO;
            List<Palavra> palavrasParaSalvar = palavrasTraduzidas.stream().map(p -> this.instanciarClassePalavra(p, categoria)).toList();

            this.salvarPalavrasNoBancoDeDados(palavrasParaSalvar);
        }
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
