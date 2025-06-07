package br.com.nat.javalingo.service;

import br.com.nat.javalingo.dto.PalavraDTO;
import br.com.nat.javalingo.enums.Categoria;
import br.com.nat.javalingo.model.DadosPalavra;
import br.com.nat.javalingo.model.Exemplo;
import br.com.nat.javalingo.model.Palavra;
import br.com.nat.javalingo.repository.PalavraRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PalavraServico {
    PalavraRepository palavraRepository;

    public PalavraServico(PalavraRepository palavraRepository){
        this.palavraRepository = palavraRepository;
    }

    private void adicionarNovosExemplos(Palavra palavra, List<Exemplo> exemplos){
        List<Exemplo> exemplosAtualizados = exemplos.stream().peek(p -> p.setPalavra(palavra)).toList();
        palavra.setExemplos(exemplosAtualizados);
        this.palavraRepository.saveAndFlush(palavra);
    }

    private Palavra instanciarClassePalavra(DadosPalavra dadosPalavra, Categoria categoria){
        Palavra palavra = new Palavra(dadosPalavra, categoria.getMin());

        List<Exemplo> exemplos = dadosPalavra.exemplos()
                .stream()
                .map(e -> new Exemplo(e.original(), e.traducao(), palavra))
                .toList();

        palavra.setExemplos(exemplos);
        return palavra;
    }

    public List<DadosPalavra> traduzirPalavra(String palavraParaTraducao){
        GeminiAPI geminiAPI = new GeminiAPI();
        return geminiAPI.buscarDadosTraducao(palavraParaTraducao);
    }

    public void salvarPalavras(List<DadosPalavra> palavras, Categoria categoria){
        List<Palavra> palavrasParaSalvar = palavras
                .stream()
                .map(p -> this.instanciarClassePalavra(p, categoria))
                .toList();

        palavrasParaSalvar.forEach(p -> {
            Optional<Palavra> palavraOptional = this.palavraRepository.buscarPalavraPorAmbosCampos(p.getOriginal(), p.getTraducao());

            if(palavraOptional.isPresent()){
                Palavra palavra = palavraOptional.get();
                this.adicionarNovosExemplos(palavra, p.getExemplos());
                System.out.println("Adicionamos novos exemplos à palavra.");
            }else{
                this.palavraRepository.save(p);
                System.out.println("tradução salva com sucesso.");
            }
        });
    }

    public void listarPalavras(){
        List<Palavra> palavras = this.palavraRepository.findAll();

        if(palavras.isEmpty()){
            System.out.println("Nenhuma palavra foi encontrada.");
            return;
        }

        Collections.sort(palavras);

        System.out.println("** lista de todas as palavras **\n");

        palavras.forEach(p -> System.out.println("""
                    Id: %s, Palavra: %s = %s
                    """.formatted(p.getId(), p.getOriginal(), p.getTraducao()))
        );
    }

    public void listarFrases(){
        List<Palavra> palavras = this.palavraRepository.findAll();

        if(palavras.isEmpty()){
            System.out.println("Nenhuma palavra foi encontrada.");
            return;
        }

        Map<Categoria, List<Exemplo>> exemplosPorCategoria = palavras
                .stream()
                .flatMap(p -> p.getExemplos().stream())
                .collect(Collectors.groupingBy(e -> Categoria.converterNivelParaCategoria(e.getPalavra().getNivelAprendizado())
                ));

        System.out.println("** todas as frases encontradas (agrupadas por categoria) **");

        exemplosPorCategoria.forEach((categoria, exemplo) -> {
            System.out.println("\n** categoria:  " + categoria);
            exemplo.forEach(e -> {
                System.out.println(e.getOriginal() + " = " + e.getTraducao());
            });
        });
    }

    public void filtrarEListarPalavrasPorCategoria(Categoria categoria){
        List<Palavra> palavras = this.palavraRepository.buscarPalavrasPorNivelAprendizado(categoria.getMin(), categoria.getMax());

        if(palavras.isEmpty()){
            System.out.println("Nenhuma palavra foi encontrada nessa categoria.");
            return;
        }

        System.out.println("** palavras da categoria **");
        palavras.forEach(p -> System.out.printf("%s = %s%n", p.getOriginal(), p.getTraducao()));
    }

    public List<Palavra> filtrarPalavrasPorCategoria(Categoria categoria){
        return this.palavraRepository.buscarPalavrasPorNivelAprendizado(categoria.getMin(), categoria.getMax());
    }

    public void buscarPalavra(String palavra){
        List<Palavra> palavras = this.palavraRepository.buscarPalavrasQueContenhamBusca(palavra.trim());

        if(palavras.isEmpty()){
            System.out.println("Nenhuma palavra foi encontrada.");
            return;
        }

        System.out.println("\n** palavras encontradas **\n");
        palavras.stream().map(PalavraDTO::criarDTO).forEach(System.out::println);
    }

    public void atualizarNivelAprendizadoPalavras(List<Palavra> palavras){
        this.palavraRepository.saveAll(palavras);
    }
}

