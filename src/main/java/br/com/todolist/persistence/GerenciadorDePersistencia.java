package br.com.todolist.persistence;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GerenciadorDePersistenciaJson {
    private final Gson gson;
    private final File arquivo;
    public GerenciadorDePersistenciaJson(String nomeArquivo) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.arquivo = new File(nomeArquivo);
    }
    

    public void salvar(Object objeto) {
        try (FileWriter writer = new FileWriter(arquivo)) {
            gson.toJson(objeto, writer);
            System.out.println("Dados salvos com sucesso em " + arquivo.getName());
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo JSON: " + e.getMessage());
        }
    }

    public Object carregar(Class<?> tipo) {
        if (!arquivo.exists() || arquivo.length() == 0) {
            System.out.println("Arquivo de dados não encontrado ou vazio. Iniciando com novos dados.");
            return null;
        }

        try (FileReader reader = new FileReader(arquivo)) {
            return gson.fromJson(reader, tipo);
        } catch (IOException e) {
            System.err.println("Erro ao carregar o arquivo JSON: " + e.getMessage());
            return null;
        } catch (JsonSyntaxException e) {
            System.err.println("Erro de sintaxe no arquivo JSON. O arquivo pode estar corrompido: " + e.getMessage());
            return null;
        }
    }
}