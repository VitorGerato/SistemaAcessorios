package view;

import controller.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * TelaFuncionario:
 * Cadastrar, editar e excluir funcionários.
 */
public class TelaFuncionario extends JFrame {

    // Tabela
    public JTextField filtroNome;
    public JComboBox<String> filtroStatus;
    public JTable tabela;
    public DefaultTableModel modelo;

    // Campos do formulário de cadastro
    public JTextField txtNomeCadastro, txtRECadastro, txtCargoCadastro, txtSenhaCadastro;
    public JButton btnCadastrar;

    // Campos do formulário de edição
    public JTextField txtNomeEdicao, txtREEdicao, txtCargoEdicao, txtSenhaEdicao;
    public JComboBox<String> comboStatusEdicao;
    public JButton btnEditar;

    // Botões de ação comuns
    public JButton btnExcluir;
    public JButton btnTrocarParaCadastrar, btnTrocarParaEditar;

    // Painel com CardLayout para alternar entre cadastro e edição
    public JPanel painelCard;
    public CardLayout cardLayout;

    public TelaFuncionario() {
        // Configurações da janela
        setTitle("Sistema de Gestão - Funcionários");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Estilo global
        Color vermelho = new Color(153, 0, 51);
        Font fonteBotao = new Font("Arial", Font.BOLD, 14);
        Color corTexto = Color.WHITE;

        // Cria o menu lateral com os botões de navegação entre as telas do sistema.
        JPanel painelLateral = new JPanel(new BorderLayout());
        painelLateral.setBackground(Color.DARK_GRAY);
        painelLateral.setPreferredSize(new Dimension(200, 750));

        // Painel superior com logo e botões
        JPanel painelTopo = new JPanel();
        painelTopo.setBackground(Color.DARK_GRAY);
        painelTopo.setLayout(new BoxLayout(painelTopo, BoxLayout.Y_AXIS));

        // Adiciona logo no topo
        JLabel logo = new JLabel();
        ImageIcon icon = new ImageIcon(getClass().getResource("/img/logoEscola.png"));
        Image imagemRedimensionada = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        logo.setIcon(new ImageIcon(imagemRedimensionada));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelTopo.add(Box.createVerticalStrut(20));
        painelTopo.add(logo);
        painelTopo.add(Box.createVerticalStrut(15));

        // Botões (exceto "Sair")
        String[] botoes = {"Funcionários", "Itens", "Saidas/Devoluções", "Reservas", "Manutenção", "Relatórios"};
        Dimension tamanhoBotao = new Dimension(180, 45);

        for (String nome : botoes) {
            JButton botao = new JButton(nome);
            botao.setMaximumSize(tamanhoBotao);
            botao.setPreferredSize(tamanhoBotao);
            botao.setMinimumSize(tamanhoBotao);
            botao.setFocusPainted(false);
            botao.setAlignmentX(Component.CENTER_ALIGNMENT);
            botao.setBackground(vermelho);
            botao.setForeground(corTexto);
            botao.setFont(fonteBotao);

            painelTopo.add(Box.createVerticalStrut(15));
            painelTopo.add(botao);

            switch (nome) {
                case "Funcionários" -> botao.addActionListener(e -> {
                    dispose();
                    TelaFuncionario tela = new TelaFuncionario();
                    new FuncionarioController(tela);
                    tela.setVisible(true);
                });
                case "Itens" -> botao.addActionListener(e -> {
                    dispose();
                    TelaItem tela = new TelaItem();
                    new ItemController(tela);
                    tela.setVisible(true);
                });
                case "Saidas/Devoluções" -> botao.addActionListener(e -> {
                    dispose();
                    TelaSaidasDevolucoes tela = new TelaSaidasDevolucoes();
                    new SaidasDevolucoesController(tela);
                    tela.setVisible(true);
                });
                case "Reservas" -> botao.addActionListener(e -> {
                    dispose();
                    TelaReserva tela = new TelaReserva();
                    new ReservaController(tela);
                    tela.setVisible(true);
                });
                case "Manutenção" -> botao.addActionListener(e -> {
                    dispose();
                    TelaManutencao tela = new TelaManutencao();
                    new ManutencaoController(tela);
                    tela.setVisible(true);
                });
                case "Relatórios" -> botao.addActionListener(e -> {
                    dispose();
                    TelaRelatorio tela = new TelaRelatorio();
                    new RelatorioController(tela);
                    tela.setVisible(true);
                });
            }
        }

        // Painel inferior com botão Sair
        JPanel painelInferior = new JPanel();
        painelInferior.setBackground(Color.DARK_GRAY);
        painelInferior.setLayout(new BoxLayout(painelInferior, BoxLayout.Y_AXIS));
        painelInferior.add(Box.createVerticalStrut(15));

        JButton btnSair = new JButton("Sair");
        btnSair.setMaximumSize(tamanhoBotao);
        btnSair.setPreferredSize(tamanhoBotao);
        btnSair.setMinimumSize(tamanhoBotao);
        btnSair.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSair.setBackground(vermelho);
        btnSair.setForeground(corTexto);
        btnSair.setFont(fonteBotao);
        btnSair.addActionListener(e -> System.exit(0));
        painelInferior.add(btnSair);

        // Adiciona os dois blocos ao painel principal
        painelLateral.add(painelTopo, BorderLayout.NORTH);
        painelLateral.add(painelInferior, BorderLayout.SOUTH);

        // Cria o painel superior contendo o título da página.
        JPanel painelTitulo = new JPanel();
        painelTitulo.setBackground(Color.LIGHT_GRAY);
        painelTitulo.setPreferredSize(new Dimension(0, 50));
        JLabel titulo = new JLabel("Cadastro de Funcionários");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        painelTitulo.add(titulo);

        // Botões para alternar entre cadastro e edição
        btnTrocarParaCadastrar = new JButton("Cadastrar");
        btnTrocarParaEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");

        JPanel painelTopoBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        for (JButton botao : new JButton[]{btnTrocarParaCadastrar, btnTrocarParaEditar, btnExcluir}) {
            botao.setBackground(vermelho);
            botao.setForeground(corTexto);
            botao.setFont(fonteBotao);
            painelTopoBotoes.add(botao);
        }

        // Painel de formulário de cadastro
        txtNomeCadastro = new JTextField();
        txtRECadastro = new JTextField();
        txtCargoCadastro = new JTextField();
        txtSenhaCadastro = new JTextField();
        btnCadastrar = new JButton("Registrar Cadastro");
        btnCadastrar.setBackground(vermelho);
        btnCadastrar.setForeground(corTexto);
        btnCadastrar.setFont(fonteBotao);

        // Painel que contém os campos e botão de cadastro de funcionário.
        JPanel painelCadastro = new JPanel(new GridLayout(5, 2, 10, 10));
        painelCadastro.setBorder(BorderFactory.createTitledBorder("Cadastrar Funcionário"));
        painelCadastro.add(new JLabel("Nome:"));
        painelCadastro.add(txtNomeCadastro);
        painelCadastro.add(new JLabel("RE:"));
        painelCadastro.add(txtRECadastro);
        painelCadastro.add(new JLabel("Cargo:"));
        painelCadastro.add(txtCargoCadastro);
        painelCadastro.add(new JLabel("Senha:"));
        painelCadastro.add(txtSenhaCadastro);
        painelCadastro.add(new JLabel());
        painelCadastro.add(btnCadastrar);


        // Painel de formulário de edição
        txtNomeEdicao = new JTextField();
        txtREEdicao = new JTextField();
        txtCargoEdicao = new JTextField();
        txtSenhaEdicao = new JTextField();
        comboStatusEdicao = new JComboBox<>(new String[]{"positivo", "negativo"});
        btnEditar = new JButton("Registrar Edição");
        btnEditar.setBackground(vermelho);
        btnEditar.setForeground(corTexto);
        btnEditar.setFont(fonteBotao);

        // Painel que contém os campos e botão de edição de funcionário.
        JPanel painelEdicao = new JPanel(new GridLayout(6, 2, 10, 10));
        painelEdicao.setBorder(BorderFactory.createTitledBorder("Editar Funcionário"));
        painelEdicao.add(new JLabel("Nome:"));
        painelEdicao.add(txtNomeEdicao);
        painelEdicao.add(new JLabel("RE:"));
        painelEdicao.add(txtREEdicao);
        painelEdicao.add(new JLabel("Cargo:"));
        painelEdicao.add(txtCargoEdicao);
        painelEdicao.add(new JLabel("Senha:"));
        painelEdicao.add(txtSenhaEdicao);
        painelEdicao.add(new JLabel("Status:"));
        painelEdicao.add(comboStatusEdicao);
        painelEdicao.add(new JLabel());
        painelEdicao.add(btnEditar);

        // Painel com CardLayout para alternância
        cardLayout = new CardLayout();
        painelCard = new JPanel(cardLayout);
        painelCard.add(painelCadastro, "cadastro");
        painelCard.add(painelEdicao, "edicao");

        // Eventos dos botões de troca de formulário
        btnTrocarParaCadastrar.addActionListener(e -> cardLayout.show(painelCard, "cadastro"));
        btnTrocarParaEditar.addActionListener(e -> cardLayout.show(painelCard, "edicao"));

        // Tabela de exibição
        modelo = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Nome", "RE", "Cargo", "Senha", "Status"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        tabela.setRowHeight(28);
        tabela.setFont(new Font("Arial", Font.PLAIN, 14));
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        JScrollPane scrollTabela = new JScrollPane(tabela);

        // Painel de conteúdo da direita (título + formulários + tabela)
        JPanel painelCentro = new JPanel(new BorderLayout());
        painelCentro.add(painelTitulo, BorderLayout.NORTH);
        painelCentro.add(painelTopoBotoes, BorderLayout.CENTER);
        painelCentro.add(painelCard, BorderLayout.SOUTH);

        JPanel painelDireito = new JPanel(new BorderLayout());
        painelDireito.add(painelCentro, BorderLayout.NORTH);

        // Painel de filtros
        filtroNome = new JTextField(20);
        filtroStatus = new JComboBox<>(new String[]{"Todos", "positivo", "negativo"});
        JPanel painelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelFiltros.add(new JLabel("Buscar Nome:"));
        painelFiltros.add(filtroNome);
        painelFiltros.add(new JLabel("Status:"));
        painelFiltros.add(filtroStatus);

        // Agrupamento superior com título, botões, formulário e filtros
        JPanel painelSuperiorCompleto = new JPanel();
        painelSuperiorCompleto.setLayout(new BorderLayout());

        JPanel painelTopoTudo = new JPanel(new BorderLayout());
        painelTopoTudo.add(painelTitulo, BorderLayout.NORTH);
        painelTopoTudo.add(painelTopoBotoes, BorderLayout.CENTER);
        painelTopoTudo.add(painelCard, BorderLayout.SOUTH);

        painelSuperiorCompleto.add(painelTopoTudo, BorderLayout.NORTH);
        painelSuperiorCompleto.add(painelFiltros, BorderLayout.SOUTH);

        painelDireito.add(painelSuperiorCompleto, BorderLayout.NORTH);
        painelDireito.add(scrollTabela, BorderLayout.CENTER);

        // Divisão entre menu e conteúdo
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelLateral, painelDireito);
        splitPane.setDividerLocation(200);
        splitPane.setEnabled(false);

        add(splitPane);
        setVisible(true);
    }
}