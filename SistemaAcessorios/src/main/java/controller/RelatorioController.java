package controller;

import model.dao.EmprestimoDAO;
import view.TelaRelatorio;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

/**
 * Controller da TelaRelatorio.
 * Responsável por gerar relatórios em CSV com:
 * - Itens mais utilizados
 * - Funcionários mais ativos
 * - Empréstimos em aberto
 */
public class RelatorioController {

    private TelaRelatorio view;
    private EmprestimoDAO dao;

    public RelatorioController(TelaRelatorio view) {
        this.view = view;
        this.dao = new EmprestimoDAO();

        this.view.btnExportar.addActionListener(e -> exportarCSV());
    }

    // Método responsável por exportar o relatório em formato CSV
    public void exportarCSV() {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Salvar Relatório CSV");
            chooser.setSelectedFile(new File("relatorio.csv"));
            int userSelection = chooser.showSaveDialog(view);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = chooser.getSelectedFile();
                FileWriter writer = new FileWriter(fileToSave.getAbsolutePath());

                // Seção: Itens mais utilizados
                writer.write("###############################\n");
                writer.write("       ITENS MAIS UTILIZADOS       \n");
                writer.write("###############################\n");
                writer.write("Item ; Quantidade de Utilizacoes\n");
                Map<String, Integer> itens = dao.getItensMaisUtilizados();
                for (Map.Entry<String, Integer> entry : itens.entrySet()) {
                    writer.write(entry.getKey() + ";" + entry.getValue() + "\n");
                }
                writer.write("\n\n");

                // Seção: Funcionários mais ativos
                writer.write("###############################\n");
                writer.write("    FUNCIONARIOS MAIS ATIVOS     \n");
                writer.write("###############################\n");
                writer.write("Funcionario ; Quantidade de Emprestimos\n");
                Map<String, Integer> funcionarios = dao.getFuncionariosMaisAtivos();
                for (Map.Entry<String, Integer> entry : funcionarios.entrySet()) {
                    writer.write(entry.getKey() + ";" + entry.getValue() + "\n");
                }
                writer.write("\n\n");

                // Seção: Empréstimos em aberto
                writer.write("###############################\n");
                writer.write("     EMPRESTIMOS EM ABERTO        \n");
                writer.write("###############################\n");
                writer.write("ID ; ID Funcionario ; ID Item ; Data Retirada ; Data Prevista\n");
                List<model.bean.Emprestimo> emAberto = dao.getEmprestimosEmAberto();
                for (model.bean.Emprestimo e : emAberto) {
                    for (model.bean.EmprestimoItem item : e.getItens()) {
                        writer.write(e.getId() + ";" + e.getIdFuncionario() + ";" + item.getIdItem() + ";" +
                                e.getDataRetirada() + ";" + e.getDataPrevista() + "\n");
                    }
                }

                writer.close();
                JOptionPane.showMessageDialog(view, "Relatório salvo com sucesso em: " + fileToSave.getAbsolutePath());
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erro ao gerar relatório: " + ex.getMessage());
        }
    }
}