package controller;

import model.bean.Item;
import model.dao.ItemDAO;
import model.dao.ManutencaoDAO;
import view.TelaItem;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ItemController {

    private TelaItem view;
    private ItemDAO dao;
    private ManutencaoDAO manutencaoDAO;

    public ItemController(TelaItem view) {
        this.view = view;
        this.dao = new ItemDAO();
        this.manutencaoDAO = new ManutencaoDAO();

        configurarEventos();
        carregarTabela();
    }

    // Configura os eventos dos botões e campos da interface gráfica.
    private void configurarEventos() {
        view.btnCadastrar.addActionListener(e -> cadastrarItem());
        view.btnEditar.addActionListener(e -> editarItem());
        view.btnExcluir.addActionListener(e -> {
            int linha = view.tabela.getSelectedRow();
            if (linha >= 0) {
                int id = Integer.parseInt(view.modelo.getValueAt(linha, 0).toString());
                int confirm = JOptionPane.showConfirmDialog(null, "ATENÇÃO: Ao excluir este item, todas as reservas, empréstimos e registros associados a ele também serão permanentemente removidos.\n\nDeseja continuar?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    ItemDAO dao = new ItemDAO();
                    dao.excluirItemComDependencias(id);
                    carregarTabela();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um item para excluir.");
            }
        });

        view.tabela.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = view.tabela.getSelectedRow();
                if (row >= 0) {
                    view.txtNomeEdicao.setText(view.modelo.getValueAt(row, 1).toString());
                    view.comboCategoriaEdicao.setSelectedItem(view.modelo.getValueAt(row, 2).toString());
                    view.comboStatusEdicao.setSelectedItem(view.modelo.getValueAt(row, 3).toString());
                }
            }
        });

        view.filtroCategoria.addActionListener(e -> carregarTabela());

        view.campoBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                carregarTabela();
            }
        });
    }

    private void carregarTabela() {
        view.modelo.setRowCount(0);
        String filtroCategoria = view.filtroCategoria.getSelectedItem().toString();
        String termoBusca = view.campoBusca.getText().trim().toLowerCase();

        List<Item> itens = dao.read();

        for (Item item : itens) {
            boolean correspondeCategoria = filtroCategoria.equals("Todos") || item.getCategoria().equalsIgnoreCase(filtroCategoria);
            boolean correspondeBusca = item.getNome().toLowerCase().contains(termoBusca);

            if (correspondeCategoria && correspondeBusca) {
                view.modelo.addRow(new Object[]{
                        item.getId(),
                        item.getNome(),
                        item.getCategoria(),
                        item.getStatus()
                });
            }
        }
    }

    // Obtém os dados do formulário de cadastro e insere no banco de dados.
    private void cadastrarItem() {
        String nome = view.txtNomeCadastro.getText().trim();
        String categoria = view.comboCategoriaCadastro.getSelectedItem().toString();

        if (!nome.matches("[a-zA-Z0-9áéíóúÁÉÍÓÚãõÃÕçÇ ]+")) {
            JOptionPane.showMessageDialog(null, "O nome não pode conter números ou caracteres especiais.");
            return;
        }

        if (nome.isEmpty() || categoria.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Preencha todos os campos para cadastrar o item.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Item i = new Item();
        i.setNome(nome);
        i.setCategoria(categoria);
        i.setStatus("ativo");

        dao.create(i);
        carregarTabela();
        limparCampos();
        JOptionPane.showMessageDialog(view, "Item cadastrado com sucesso!");
    }

    // Edita o item selecionado e pode enviá-lo para manutenção.
    private void editarItem() {
        int row = view.tabela.getSelectedRow();
        if (row >= 0) {
            int id = Integer.parseInt(view.modelo.getValueAt(row, 0).toString());
            String nome = view.txtNomeEdicao.getText();
            String categoria = view.comboCategoriaEdicao.getSelectedItem().toString();
            String status = view.comboStatusEdicao.getSelectedItem().toString();

            if (!nome.matches("[a-zA-Z0-9áéíóúÁÉÍÓÚãõÃÕçÇ ]+")) {
                JOptionPane.showMessageDialog(null, "O nome não pode conter caracteres especiais.");
                return;
            }

            if (status.equalsIgnoreCase("em manutenção")) {
                String dataEntrada = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                JTextArea obsField = new JTextArea(5, 20);
                obsField.setLineWrap(true);
                obsField.setWrapStyleWord(true);
                JScrollPane scrollPane = new JScrollPane(obsField);

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.add(new JLabel("Data atual: " + dataEntrada));

                panel.add(Box.createVerticalStrut(5));

                panel.add(new JLabel("Observação (opcional):"));
                panel.add(scrollPane);

                int result = JOptionPane.showConfirmDialog(null, panel, "Enviar para Manutenção",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String observacao = obsField.getText();

                    if (dataEntrada.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "A data de entrada é obrigatória.");
                        return;
                    }

                    Item item = new Item();
                    item.setId(id);
                    item.setNome(nome);
                    item.setCategoria(categoria);
                    item.setStatus(status);
                    dao.update(item);

                    ManutencaoDAO manutencaoDAO = new ManutencaoDAO();
                    java.sql.Date data = java.sql.Date.valueOf(dataEntrada);
                    manutencaoDAO.enviarParaManutencao(id, data, observacao);

                    JOptionPane.showMessageDialog(null, "Item enviado para manutenção.");
                    carregarTabela();
                    return;
                } else {
                    return;
                }
            }

            // Edição normal (sem envio para manutenção)
            Item item = new Item();
            item.setId(id);
            item.setNome(nome);
            item.setCategoria(categoria);
            item.setStatus(status);
            dao.update(item);
            JOptionPane.showMessageDialog(null, "Item atualizado.");
            carregarTabela();
        } else {
            JOptionPane.showMessageDialog(null, "Selecione um item para editar.");
        }
    }

    // Limpa os campos após o cadastro / edição
    private void limparCampos() {
        view.txtNomeCadastro.setText("");
        view.txtNomeEdicao.setText("");
        view.comboCategoriaCadastro.setSelectedIndex(0);
        view.comboCategoriaEdicao.setSelectedIndex(0);
        view.comboStatusEdicao.setSelectedIndex(0);
    }

}
