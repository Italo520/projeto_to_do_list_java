// Em: src/main/java/br/com/todolist/persistence/GerenciadorDePersistenciaJson.java
package br.com.todolist.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class GerenciadorDePersistenciaJson {

    // 1. Substitui o Gson pelo ObjectMapper do Jackson
    private final ObjectMapper objectMapper;
    private final File arquivo;

    public GerenciadorDePersistenciaJson(String nomeArquivo) {
        // 2. Cria o ObjectMapper e registra o módulo para suportar o java.time
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .enable(SerializationFeature.INDENT_OUTPUT); // Opcional: para formatar o JSON de forma legível
        this.arquivo = new File(nomeArquivo);
    }

    public void salvar(Object objeto) {
        try (FileWriter writer = new FileWriter(arquivo)) {
            // 3. Usa o método writeValue do Jackson para serializar o objeto
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
            // 4. Usa o método readValue do Jackson para deserializar
            return objectMapper.readValue(reader, objectMapper.constructType(tipoDeDados));
        } catch (IOException e) {
            System.err.println("Erro ao carregar o arquivo JSON: " + e.getMessage());
            return null;
        }
    }
}