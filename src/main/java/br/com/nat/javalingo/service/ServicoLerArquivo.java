package br.com.nat.javalingo.service;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ServicoLerArquivo {

    public String lerArquivo(String nomeArquivo, String tipoArquivo) throws IOException{
        String path = nomeArquivo.concat(".").concat(tipoArquivo);

        try{
            InputStream input = getClass().getClassLoader().getResourceAsStream(path);
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new FileNotFoundException("Arquivo não encontrado.");
        }
    }
}
