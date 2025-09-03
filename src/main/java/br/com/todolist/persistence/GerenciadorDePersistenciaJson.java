// Em: src/main/java/br/com/todolist/persistence/GerenciadorDePersistenciaJson.java
package br.com.todolist.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public class GerenciadorDePersistenciaJson {

    private final ObjectMapper objectMapper;
    private final File arquivo;

    public GerenciadorDePersistenciaJson(String nomeArquivo) {
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .enable(SerializationFeature.INDENT_OUTPUT);
        this.arquivo = new File(nomeArquivo);
    }

    public void salvar(Object objeto) {
        try (FileWriter writer = new FileWriter(arquivo)) {
            objectMapper.writeValue(writer, objeto);
            System.out.println("Dados salvos com sucesso em " + arquivo.getName());
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo JSON: " + e.getMessage());
        }
    }

    public <T> T carregar(Type tipoDeDados) {
        if (!arquivo.exists() || arquivo.length() == 0) {
            return null;
        }

        try (FileReader reader = new FileReader(arquivo)) {
            return objectMapper.readValue(reader, objectMapper.constructType(tipoDeDados));
        } catch (IOException e) {
            System.err.println("Erro ao carregar o arquivo JSON: " + e.getMessage());
            return null;
        }
    }
}