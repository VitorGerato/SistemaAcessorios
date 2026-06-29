package controller;

import model.bean.Funcionario;
import model.dao.FuncionarioDAO;
import view.TelaFuncionario;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Controller responsável por conectar a TelaFuncionario
 * Gerencia os eventos da interface, integração com o DAO e lógica de CRUD.
 */
public class FuncionarioController {

    private TelaFuncionario view;
    private FuncionarioDAO dao;

    public FuncionarioController(TelaFuncionario view) {
        this.view = view;
        this.dao = new FuncionarioDAO();

        configurarEventos();
        carregarTabela();
    }

    // Ações aos botões da interface e à seleção da tabela.
    private void configurarEventos() {
        // Ação para cadastrar novo funcionário
        view.btnCadastrar.addActionListener(e -> cadastrarFuncionario());

        // Ação para editar funcionário selecionado
        view.btnEditar.addActionListener(e -> editarFuncionario());

        // Ação para excluir funcionário selecionado
        view.btnExcluir.addActionListener(e -> excluirFuncionario());

        // Ação ao digitar no filtro de nome
        view.filtroNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                carregarTabela();
            }
        });

        // Ação ao mudar o filtro de status
        view.filtroStatus.addActionListener(e -> carregarTabela());

        // Preencher campos ao clicar na tabela
        view.tabela.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = view.tabela.getSelectedRow();
                if (row >= 0) {
                    view.txtNomeEdicao.setText(view.modelo.getValueAt(row, 1).toString());
                    view.txtREEdicao.setText(view.modelo.getValueAt(row, 2).toString());
                    view.txtCargoEdicao.setText(view.modelo.getValueAt(row, 3).toString());
                    view.txtSenhaEdicao.setText(view.modelo.getValueAt(row, 4).toString());
                    view.comboStatusEdicao.setSelectedItem(view.modelo.getValueAt(row, 5).toString());
                }
            }
        });
    }

    // Preenche a tabela da interface com os dados da base (com filtros).
    private void carregarTabela() {
        view.modelo.setRowCount(0);
        List<Funcionario> funcionarios = dao.read();

        String filtroNome = view.filtroNome.getText().toLowerCase();
        String filtroStatus = view.filtroStatus.getSelectedItem().toString().toLowerCase();

        for (Funcionario f : funcionarios) {
            boolean nomeCombina = f.getNome().toLowerCase().contains(filtroNome);
            boolean statusCombina = filtroStatus.equals("todos") || f.getStatus().equalsIgnoreCase(filtroStatus);

            if (nomeCombina && statusCombina) {
                view.modelo.addRow(new Object[]{
                        f.getId(),
                        f.getNome(),
                        f.getRe(),
                        f.getCargo(),
                        f.getSenha(),
                        f.getStatus()
                });
            }
        }
    }

    // Pega os dados dos campos de cadastro e insere um novo funcionário.
    private void cadastrarFuncionario() {
        String nome = view.txtNomeCadastro.getText().trim();
        String re = view.txtRECadastro.getText().trim();
        String cargo = view.txtCargoCadastro.getText().trim();
        String senha = view.txtSenhaCadastro.getText().trim();

        if (!nome.matches("[a-zA-ZáéíóúÁÉÍÓÚãõÃÕçÇ ]+")) {
            JOptionPane.showMessageDialog(null, "O nome não pode conter números ou caracteres especiais.");
            return;
        }

        if (!re.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "O RE deve conter apenas números.");
            return;
        }

        if (dao.reExiste(re, null)) {
            JOptionPane.showMessageDialog(null, "Já existe um funcionário com esse RE.");
            return;
        }

        if (nome.isEmpty() || re.isEmpty() || cargo.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Preencha todos os campos para cadastrar o funcionário.", "Campos obrigatórios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cargo.equalsIgnoreCase("Administrador") || cargo.equalsIgnoreCase("administrador") ) {
            JOptionPane.showMessageDialog(view, "Não é permitido cadastrar um funcionário como Administrador.");
            return;
        }

        Funcionario f = new Funcionario();
        f.setNome(view.txtNomeCadastro.getText());
        f.setRe(view.txtRECadastro.getText());
        f.setCargo(view.txtCargoCadastro.getText());
        f.setSenha(view.txtSenhaCadastro.getText());
        f.setStatus("positivo");

        dao.create(f);
        carregarTabela();
        limparCampos();

        JOptionPane.showMessageDialog(view, "Funcionário cadastrado com sucesso!");
    }

    // Atualiza o funcionário selecionado com os dados da tabela.
    private void editarFuncionario() {
        String nome = view.txtNomeEdicao.getText().trim();
        String re = view.txtREEdicao.getText().trim();
        String cargo = view.txtCargoEdicao.getText().trim();
        String senha = view.txtSenhaEdicao.getText().trim();
        int row = view.tabela.getSelectedRow();

        if (row >= 0) {
            int id = Integer.parseInt(view.modelo.getValueAt(row, 0).toString());

            if (!nome.matches("[a-zA-ZáéíóúÁÉÍÓÚãõÃÕçÇ ]+")) {
                JOptionPane.showMessageDialog(null, "O nome não pode conter números ou caracteres especiais.");
                return;
            }

            if (!re.matches("\\d+")) {
                JOptionPane.showMessageDialog(null, "O RE deve conter apenas números.");
                return;
            }

            if (dao.reExiste(re, id)) {
                JOptionPane.showMessageDialog(null, "Esse RE já está em uso por outro funcionário.");
                return;
            }

            if (nome.isEmpty() || re.isEmpty() || cargo.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
                return;
            }

            if (cargo.equalsIgnoreCase("Administrador") || cargo.equalsIgnoreCase("administrador") ) {
                JOptionPane.showMessageDialog(view, "Não é permitido transformar um funcionário em Administrador.");
                return;
            }

            Funcionario f = new Funcionario();
            f.setId(id);
            f.setNome(view.txtNomeEdicao.getText());
            f.setRe(view.txtREEdicao.getText());
            f.setCargo(view.txtCargoEdicao.getText());
            f.setSenha(view.txtSenhaEdicao.getText());
            f.setStatus(view.comboStatusEdicao.getSelectedItem().toString());

            dao.update(f);
            carregarTabela();

            JOptionPane.showMessageDialog(view, "Informações editadas com sucesso!");
        }
    }

    // Exclui o funcionário selecionado.
    private void excluirFuncionario() {
        int row = view.tabela.getSelectedRow();
        if (row >= 0) {
            int id = Integer.parseInt(view.modelo.getValueAt(row, 0).toString());

            int confirmacao = JOptionPane.showConfirmDialog(view,
                    "ATENÇÃO: Ao excluir este funcionário, todas as reservas, empréstimos e registros associados a ele também serão permanentemente removidos.\n\nDeseja continuar?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirmacao == JOptionPane.YES_OPTION) {
                dao.delete(id);
                carregarTabela();
                JOptionPane.showMessageDialog(view, "Funcionário excluído com sucesso.");
            }
        } else {
            JOptionPane.showMessageDialog(view, "Selecione um funcionário para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Limpa os campos após o cadastro / edição
    private void limparCampos() {
        view.txtNomeCadastro.setText("");
        view.txtNomeEdicao.setText("");
        view.txtRECadastro.setText("");
        view.txtREEdicao.setText("");
        view.txtCargoCadastro.setText("");
        view.txtCargoEdicao.setText("");
        view.txtSenhaCadastro.setText("");
        view.txtSenhaEdicao.setText("");
        view.comboStatusEdicao.setSelectedIndex(0);
    }
}