package br.com.nat.javalingo.service;

import br.com.nat.javalingo.model.DadosPalavra;
import com.google.common.collect.ImmutableList;
import com.google.genai.Client;
import com.google.genai.ResponseStream;
import com.google.genai.types.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class GeminiAPI {
    private final String API_KEY = System.getenv("GEMINI_API_KEY");
    private final Client client = Client.builder().apiKey(API_KEY).build();

    private GenerateContentConfig configAPI() {
        try{
            String instrucoesGemini = new ServicoLerArquivo().lerArquivo("instrucoesGemini", "json");

            return
                GenerateContentConfig
                    .builder()
                    .responseMimeType("application/json")
                    .responseSchema(Schema.fromJson(instrucoesGemini.toString()))
                    .systemInstruction(
                        Content
                            .fromParts(
                                    Part.fromText("user is gonna give a word in portuguese or english and you must translate it (if is in English translate to Portuguese, and if is in Portuguese translate to English). We know that some words have different means, so you can return as answer an array with the maximum of three translated words.")
                            )
                    ).build();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DadosPalavra> buscarDadosTraducao(String palavraParaTraducao) {
        List<Content> contents = ImmutableList.of(
            Content.builder()
                .role("user")
                .parts(ImmutableList.of(Part.fromText(palavraParaTraducao)))
                .build()
        );

        String MODEL_NAME = "gemini-2.5-flash-preview-04-17";
        ResponseStream<GenerateContentResponse> responseStream = client.models.generateContentStream(MODEL_NAME, contents, this.configAPI());

        StringBuilder respostaCompleta = new StringBuilder();

        for (GenerateContentResponse res : responseStream) {
            if (res.candidates().isEmpty() || res.candidates().get().get(0).content().isEmpty() || res.candidates().get().get(0).content().get().parts().isEmpty()) {
                continue;
            }

            List<Part> parts = res.candidates().get().get(0).content().get().parts().get();
            for (Part part : parts) {
                part.text().ifPresent(respostaCompleta::append);
            }
        }

        responseStream.close();
        String json = respostaCompleta.toString();
        return new Gson().fromJson(json, new TypeToken<List<DadosPalavra>>(){}.getType());
    }
}
