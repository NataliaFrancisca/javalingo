package br.com.nat.javalingo.service;

import br.com.nat.javalingo.model.DadosPalavra;
import br.com.nat.javalingo.model.DadosTraducao;

import java.util.List;

public class TraducaoServico {

    private List<DadosPalavra> traduzirPalavra(String palavraParaTraducao){
        GeminiAPI geminiAPI = new GeminiAPI();
        return geminiAPI.buscarDadosTraducao(palavraParaTraducao);
    }

    public List<DadosPalavra> traduzir(String palavraParaTraducao){
        List<DadosPalavra> palavrasTraduzidas = this.traduzirPalavra(palavraParaTraducao);

        if(palavrasTraduzidas == null){
            System.out.println("Erro ao tentar traduzir a palavra indicada.");
            return null;
        }

        palavrasTraduzidas.forEach(p -> {
            DadosTraducao exemplo = p.exemplos().getFirst();

            System.out.printf("""
                    %s = %s
                    exemplo: %s = %s
                    %n""", p.original(), p.traducao(), exemplo.original(), exemplo.traducao()
            );
        });

        return palavrasTraduzidas;
    }
}
