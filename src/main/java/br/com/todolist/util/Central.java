package br.com.todolist.util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Central {
    public static void gerarPdf(String nomeArquivo, String titulo, String[] cabecalhos, List<String[]> dados) {
        try (PdfWriter writer = new PdfWriter(nomeArquivo)) {
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Adiciona o título
            document.add(new Paragraph(titulo).setFontSize(20).setTextAlignment(TextAlignment.CENTER));

            // Adiciona um espaço
            document.add(new Paragraph("\n"));

            // Adiciona a tabela com os cabeçalhos
            Table table = new Table(cabecalhos.length);
            for (String cabecalho : cabecalhos) {
                table.addHeaderCell(cabecalho);
            }

            // Adiciona os dados
            for (String[] linha : dados) {
                for (String celula : linha) {
                    table.addCell(celula);
                }
            }

            document.add(table);
            document.close();
            System.out.println("PDF gerado com sucesso: " + nomeArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao gerar o PDF: " + e.getMessage());
        }
    }

    public static void gerarExcel(String nomeArquivo, String nomePlanilha, String[] cabecalhos, List<String[]> dados, List<String> colunaExtra) {
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream outputStream = new FileOutputStream(nomeArquivo)) {

            Sheet sheet = workbook.createSheet(nomePlanilha);

            // Valida se a quantidade de linhas de dados e da coluna extra é a mesma
            if (dados.size() != colunaExtra.size()) {
                System.err.println("Erro: O número de linhas de dados não corresponde ao número de itens na coluna extra.");
                return;
            }

            // Adiciona os cabeçalhos
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < cabecalhos.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(cabecalhos[i]);
            }

            // Adiciona os dados e a coluna extra
            int rowNum = 1;
            for (int i = 0; i < dados.size(); i++) {
                String[] linhaDados = dados.get(i);
                String textoExtra = colunaExtra.get(i);

                Row row = sheet.createRow(rowNum++);
                // Popula as células com os dados da lista
                for (int j = 0; j < linhaDados.length; j++) {
                    row.createCell(j).setCellValue(linhaDados[j]);
                }
                // Adiciona a coluna extra na última posição
                row.createCell(linhaDados.length).setCellValue(textoExtra);
            }
            
            // Ajusta a largura das colunas
            for (int i = 0; i < cabecalhos.length + 1; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            System.out.println("Excel gerado com sucesso: " + nomeArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao gerar o Excel: " + e.getMessage());
        }
    }
}