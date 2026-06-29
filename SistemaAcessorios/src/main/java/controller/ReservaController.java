package controller;

import model.bean.Reserva;
import model.dao.ItemDAO;
import model.dao.ReservaDAO;
import view.TelaReserva;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReservaController {
    private TelaReserva view;
    private ReservaDAO dao;

    public ReservaController(TelaReserva view) {
        this.view = view;
        this.dao = new ReservaDAO();

        configurarEventos();
        carregarTabelas();
    }

    // Configura os eventos da interface
    private void configurarEventos() {
        view.filtroItem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                carregarTabelas();
            }
        });

        view.btnFecharReserva.addActionListener(e -> FecharReservaAceita());
    }

    // Carrega dados nas tabelas e cards conforme filtros
    private void carregarTabelas() {
        view.modeloAceitas.setRowCount(0);
        view.painelCards.removeAll();

        String textoFiltro = view.filtroItem.getText().toLowerCase();

        List<Reserva> lista = dao.readComNomes();

        for (Reserva r : lista) {
            boolean nomeCondiz = r.getNomeItem().toLowerCase().contains(textoFiltro);
            boolean statusCondiz = r.getStatus().equalsIgnoreCase("aceita") || r.getStatus().equalsIgnoreCase("pendente") || r.getStatus().equalsIgnoreCase("fechada");

            if (nomeCondiz && statusCondiz) {
                Object[] linha = {
                        r.getId(), r.getNomeItem(), r.getNomeFuncionario(), r.getDataReserva(), r.getStatus()
                };

                if (r.getStatus().equalsIgnoreCase("aceita") || r.getStatus().equalsIgnoreCase("fechada")) {
                    view.modeloAceitas.addRow(linha);
                } else if (r.getStatus().equalsIgnoreCase("pendente")) {
                    JPanel card = new JPanel(new BorderLayout());
                    card.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createEmptyBorder(10, 10, 10, 10),
                            BorderFactory.createLineBorder(Color.GRAY)
                    ));
                    card.setBackground(new Color(250, 250, 250));

                    JLabel info = new JLabel(
                            "<html><b>Item:</b> " + r.getNomeItem() +
                                    " | <b>Data:</b> " + r.getDataReserva() +
                                    " | <b>Funcionário:</b> " + r.getNomeFuncionario() + "</html>");
                    info.setFont(new Font("Arial", Font.PLAIN, 14));

                    JButton btnAceitar = new JButton("Aceitar");
                    btnAceitar.setBackground(new Color(0, 153, 76));
                    btnAceitar.setForeground(Color.WHITE);

                    JButton btnRecusar = new JButton("Recusar");
                    btnRecusar.setBackground(new Color(204, 0, 0));
                    btnRecusar.setForeground(Color.WHITE);

                    JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                    botoes.add(btnAceitar);
                    botoes.add(btnRecusar);

                    card.add(info, BorderLayout.CENTER);
                    card.add(botoes, BorderLayout.SOUTH);

                    btnAceitar.addActionListener(e -> {
                        ItemDAO itemDAO = new ItemDAO();
                        String statusItem = itemDAO.getStatusDoItem(r.getIdItem());

                        if (statusItem.equalsIgnoreCase("inativo") || statusItem.equalsIgnoreCase("em manutenção")) {
                            JOptionPane.showMessageDialog(view, "Este item está inativo ou em manutenção e não pode ser reservado.");
                            return;
                        }

                        dao.aceitar(r.getId());
                        carregarTabelas();
                        JOptionPane.showMessageDialog(view, "Reserva aceita com sucesso!");
                    });

                    btnRecusar.addActionListener(e -> {
                        JTextArea observacao = new JTextArea(5, 20);
                        int resultado = JOptionPane.showConfirmDialog(view, new JScrollPane(observacao),
                                "Digite uma observação (opcional)", JOptionPane.OK_CANCEL_OPTION);

                        if (resultado == JOptionPane.OK_OPTION) {
                            dao.recusar(r.getId(), observacao.getText().trim());
                            carregarTabelas();
                            JOptionPane.showMessageDialog(view, "Reserva recusada.");
                        }
                    });

                    view.painelCards.add(card);
                    view.painelCards.add(Box.createVerticalStrut(10));
                }
            }
        }

        view.painelCards.revalidate();
        view.painelCards.repaint();
    }

    // Fecha a reserva aceita
    private void FecharReservaAceita() {
        int linhaSelecionada = view.tabelaAceitas.getSelectedRow();
        if (linhaSelecionada >= 0) {
            int idReserva = (int) view.modeloAceitas.getValueAt(linhaSelecionada, 0);
            String statusAtual = view.modeloAceitas.getValueAt(linhaSelecionada, 4).toString();

            if (statusAtual.equalsIgnoreCase("fechada")) {
                JOptionPane.showMessageDialog(view, "Esta reserva já foi finalizada.");
                return;
            }

            int opcao = JOptionPane.showConfirmDialog(null,
                    "Deseja finalizar esta reserva?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (opcao == JOptionPane.YES_OPTION) {
                ReservaDAO dao = new ReservaDAO();
                boolean sucesso = dao.fechar(idReserva);

                // Atualiza a célula da tabela diretamente
                view.modeloAceitas.setValueAt("fechada", linhaSelecionada, 4);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Selecione uma reserva na tabela.");
        }
    }
}