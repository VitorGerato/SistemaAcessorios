package controller;

import model.bean.Emprestimo;
import model.dao.EmprestimoDAO;
import model.dao.FuncionarioDAO;
import model.dao.ItemDAO;
import view.TelaSaidasDevolucoes;
import model.bean.EmprestimoItem;
import java.util.ArrayList;
import model.bean.Administrador;
import model.bean.Sessao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Controller da tela de Saídas e Devoluções.
 * Gerencia o registro de empréstimos, devoluções e filtragem da tabela.
 */
public class SaidasDevolucoesController {

    private TelaSaidasDevolucoes view;
    private EmprestimoDAO dao;

    public SaidasDevolucoesController(TelaSaidasDevolucoes view) {
        this.view = view;
        this.dao = new EmprestimoDAO();

        configurarEventos();
        carregarTabela();
    }

    /**
     * Associa os eventos aos botões e filtros.
     */
    private void configurarEventos() {
        view.btnRegistrarSaida.addActionListener(e -> registrarSaida());
        view.btnRegistrarDevolucao.addActionListener(e -> registrarDevolucao());

        view.filtroItem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                carregarTabela();
            }
        });

        view.filtroStatus.addActionListener(e -> carregarTabela());
    }

    /**
     * Carrega todos os empréstimos com nomes dos itens e funcionários.
     * Aplica filtros por nome e status.
     */
    private void carregarTabela() {
        DefaultTableModel modelo = view.modelo;
        modelo.setRowCount(0);

        String filtroNome = view.filtroItem.getText().toLowerCase();
        String statusSelecionado = view.filtroStatus.getSelectedItem().toString().toLowerCase();

        List<Emprestimo> lista = dao.readComNomes();
        ItemDAO itemDAO = new ItemDAO();

        for (Emprestimo emp : lista) {
            for (EmprestimoItem item : emp.getItens()) {
                String nomeItem = dao.buscarNomeItem(item.getIdItem());
                String nomeFuncionario = emp.getNomeFuncionario();
                String nomeAdmin = emp.getAdministrador().getNome();
                String statusItem = item.getStatusDevolucao();
                String observacoes = item.getObservacoes();

                boolean condNome = nomeItem.toLowerCase().contains(filtroNome);
                boolean condStatus = statusSelecionado.equals("todos") || statusItem.equalsIgnoreCase(statusSelecionado);

                if (condNome && condStatus) {
                    modelo.addRow(new Object[]{
                            item.getIdEmprestimoItem(),
                            nomeFuncionario,
                            nomeItem,
                            nomeAdmin,
                            emp.getDataRetirada(),
                            emp.getDataPrevista(),
                            emp.getDataDevolucao() != null ? emp.getDataDevolucao() : "",
                            statusItem,
                            observacoes
                    });
                }
            }
        }
    }

    // Registra uma saida de um item
    private void registrarSaida() {
        try {
            int idFuncionario = Integer.parseInt(view.txtIdFuncionario.getText());
            int idItem = Integer.parseInt(view.txtIdItem.getText());
            Date dataPrevista = view.dateChooserPrevista.getDate();
            String observacoes = view.txtObservacoes.getText();
            FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
            ItemDAO itemDAO = new ItemDAO();
            String statusItem = itemDAO.getStatusDoItem(idItem);

            if (statusItem.equalsIgnoreCase("em manutenção")) {
                JOptionPane.showMessageDialog(null, "Este item está em manutenção e não pode ser emprestado.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!funcionarioDAO.existeFuncionario(idFuncionario)) {
                JOptionPane.showMessageDialog(view, "Funcionário com o ID informado não existe.");
                return;
            }

            if (!itemDAO.existeItem(idItem)) {
                JOptionPane.showMessageDialog(view, "Item com o ID informado não existe.");
                return;
            }

            if (dao.itemEstaEmprestado(idItem)) {
                JOptionPane.showMessageDialog(view, "O item está emprestado e ainda não foi devolvido.");
                return;
            }

            if (dataPrevista == null) {
                JOptionPane.showMessageDialog(view, "Selecione a data prevista para devolução.");
                return;
            }

            Date hoje = new Date();
            Calendar datHoje = Calendar.getInstance();
            datHoje.setTime(hoje);
            datHoje.set(Calendar.HOUR_OF_DAY, 0);
            datHoje.set(Calendar.MINUTE, 0);
            datHoje.set(Calendar.SECOND, 0);
            datHoje.set(Calendar.MILLISECOND, 0);

            Calendar datPrevista = Calendar.getInstance();
            datPrevista.setTime(dataPrevista);
            datPrevista.set(Calendar.HOUR_OF_DAY, 0);
            datPrevista.set(Calendar.MINUTE, 0);
            datPrevista.set(Calendar.SECOND, 0);
            datPrevista.set(Calendar.MILLISECOND, 0);

            if (datPrevista.before(datHoje)) {
                JOptionPane.showMessageDialog(view, "A data prevista não pode ser anterior ao dia de hoje.");
                return;
            }

            if (dao.FuncionarioEmprestimoAberto(idFuncionario)) {
                JOptionPane.showMessageDialog(view, "O funcionário possui um empréstimo em aberto! \n Devolva antes de realizar outro.");
                return;
            }

            String status = funcionarioDAO.getStatusFuncionario(idFuncionario);
            if (status != null && status.equalsIgnoreCase("negativo")) {
                JOptionPane.showMessageDialog(view, "Funcionário com status negativo não pode registrar saídas.");
                return;
            }

            Administrador admin = Sessao.getAdministrador();
            if (admin == null) {
                JOptionPane.showMessageDialog(view, "Erro: Nenhum administrador está logado.");
                return;
            }

            Emprestimo emp = new Emprestimo();
            emp.setIdFuncionario(idFuncionario);
            emp.setDataRetirada(new Date());
            emp.setDataPrevista(dataPrevista);

            // Define o ID do administrador corretamente
            emp.setIdAdmin(admin.getId());
            emp.setAdministrador(admin);

            List<EmprestimoItem> itens = new ArrayList<>();
            EmprestimoItem item = new EmprestimoItem();
            item.setIdItem(idItem);
            item.setStatusDevolucao("pendente");
            item.setObservacoes(observacoes);
            itens.add(item);
            emp.setItens(itens);

            dao.inserirEmprestimo(emp);

            JOptionPane.showMessageDialog(view, "Saída registrada com sucesso!");
            limparCampos();
            carregarTabela();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Preencha todos os campos corretamente.");
        }
    }

    //Atualiza o empréstimo selecionado para "devolvido".
    private void registrarDevolucao() {
        int row = view.tabela.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(view, "Selecione um item para devolver.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String status = view.modelo.getValueAt(row, 7).toString();
        if (status.equalsIgnoreCase("devolvido")) {
            JOptionPane.showMessageDialog(view, "Este item já foi devolvido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idEmprestimoItem = Integer.parseInt(view.modelo.getValueAt(row, 0).toString());

        java.sql.Date dataHoje = new java.sql.Date(new java.util.Date().getTime());
        String dataFormatada = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());

        JLabel labelData = new JLabel("Data de hoje: " + dataFormatada);
        JLabel labelObs = new JLabel("Observações:");

        JTextArea campoObservacoes = new JTextArea(6, 30);
        campoObservacoes.setLineWrap(true);
        campoObservacoes.setWrapStyleWord(true);

        JPanel painelCentro = new JPanel(new BorderLayout(5, 5));
        painelCentro.add(labelObs, BorderLayout.NORTH);
        painelCentro.add(new JScrollPane(campoObservacoes), BorderLayout.CENTER);

        JPanel painel = new JPanel(new BorderLayout(5, 10));
        painel.add(labelData, BorderLayout.NORTH);
        painel.add(painelCentro, BorderLayout.CENTER);

        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        int resultado = JOptionPane.showConfirmDialog(
                view,
                painel,
                "Registrar Devolução",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resultado == JOptionPane.OK_OPTION) {
            String observacoes = campoObservacoes.getText();
            dao.marcarComoDevolvido(idEmprestimoItem, dataHoje, observacoes);
            carregarTabela();
            JOptionPane.showMessageDialog(view, "Item devolvido com sucesso.");
        }
    }

    // Limpa os campos após registrar uma saída.
    private void limparCampos() {
        view.txtIdFuncionario.setText("");
        view.txtIdItem.setText("");
        view.txtObservacoes.setText("");
        view.dateChooserPrevista.setDate(null);
    }
}