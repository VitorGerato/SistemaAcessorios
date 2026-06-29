package controller;

import model.bean.Item;
import model.dao.ItemDAO;
import model.dao.ManutencaoDAO;
import view.TelaManutencao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * Controller responsável por conectar a TelaItem
 * Gerencia os eventos da interface, integração com o DAO e lógica de manutenção.
 */
public class ManutencaoController {

    private TelaManutencao view;
    private ManutencaoDAO manutencaoDAO = new ManutencaoDAO();
    private ItemDAO itemDAO = new ItemDAO();

    public ManutencaoController(TelaManutencao view) {
        this.view = view;
        configurarEventos();
        carregarTabela();
    }

    //Eventos aos componentes da interface.
    private void configurarEventos() {

        view.btnDevolver.addActionListener(e -> {
            int row = view.tabela.getSelectedRow();
            if (row >= 0) {
                int id = Integer.parseInt(view.tabela.getValueAt(row, 0).toString());

                int confirm = JOptionPane.showConfirmDialog(
                        view,
                        "Deseja marcar este item como 'ativo' e retorná-lo?",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    itemDAO.updateStatus(id, "ativo");
                    manutencaoDAO.removerRegistro(id);
                    carregarTabela();
                    JOptionPane.showMessageDialog(view, "Item retornado com sucesso.");
                }
            } else {
                JOptionPane.showMessageDialog(view, "Selecione um item para retornar.");
            }
        });

        // Filtro digitado para nome
        view.filtroNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                carregarTabela();
            }
        });
    }

    /**
     * Carrega a tabela com itens em manutenção,
     * aplica filtros de nome e status.
     */
    private void carregarTabela() {
        DefaultTableModel modelo = (DefaultTableModel) view.tabela.getModel();
        modelo.setRowCount(0);

        String filtroNome = view.filtroNome.getText().toLowerCase();

        List<Item> lista = manutencaoDAO.readManutencao();

        for (Item i : lista) {
            boolean condNome = i.getNome().toLowerCase().contains(filtroNome);

            if (condNome) {
                modelo.addRow(new Object[]{
                        i.getId(),
                        i.getNome(),
                        i.getCategoria(),
                        i.getStatus(),
                        i.getDataEntrada(),
                        i.getObservacoes()
                });
            }
        }
    }
}