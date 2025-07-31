package br.com.poo.todolist.persistence;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.thoughtworks.xstream.XStream;

import br.com.poo.todolist.models.Evento;
import br.com.poo.todolist.models.Tarefa;


public class GerenciadorDePersistencia {

    private XStream xstream;
    private File arquivo;

  
public GerenciadorDePersistencia(String nomeArquivo) {
    this.arquivo = new File(nomeArquivo);
    this.xstream = new XStream();
    
    xstream.addPermission(com.thoughtworks.xstream.security.NoTypePermission.NONE);
    xstream.addPermission(new com.thoughtworks.xstream.security.PrimitiveTypePermission());
    xstream.allowTypeHierarchy(java.util.List.class);
    xstream.allowTypeHierarchy(java.util.ArrayList.class);
    xstream.allowTypes(new Class[]{
        br.com.poo.todolist.models.Tarefa.class,
        br.com.poo.todolist.models.Evento.class,
        br.com.poo.todolist.models.Subtarefa.class,
        br.com.poo.todolist.models.Usuario.class
    });

    xstream.alias("tarefa", br.com.poo.todolist.models.Tarefa.class);
    xstream.alias("subtarefa", br.com.poo.todolist.models.Subtarefa.class);
    xstream.alias("evento", br.com.poo.todolist.models.Evento.class);
    xstream.alias("usuario", br.com.poo.todolist.models.Usuario.class);
    

}

   
    public void salvar(List<Tarefa> tarefas, List<Evento> eventos) {
        
        Object[] dados = { tarefas, eventos };
        
        try (FileWriter writer = new FileWriter(arquivo)) {
            String xml = xstream.toXML(dados);
            writer.write(xml);
            System.out.println("Dados salvos com sucesso em " + arquivo.getName());
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo XML: " + e.getMessage());
        }
    }

  
    public Object[] carregar() {
        if (!arquivo.exists() || arquivo.length() == 0) {
            System.out.println("Arquivo de dados não encontrado ou vazio. Iniciando com novos dados.");
            return null; 
        }

        try (FileReader reader = new FileReader(arquivo)) {
            return (Object[]) xstream.fromXML(reader);
        } catch (IOException e) {
            System.err.println("Erro ao carregar o arquivo XML: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Erro na deserialização do XML. O arquivo pode estar corrompido: " + e.getMessage());
            return null;
        }
    }
}