package view;

import controller.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * TelaItem:
 * Cadastrar, editar e excluir funcionários.
 */
public class TelaItem extends JFrame {

    public JTable tabela;
    public DefaultTableModel modelo;
    public JTextField txtNomeCadastro, txtNomeEdicao, campoBusca;
    public JComboBox<String> comboCategoriaCadastro, comboCategoriaEdicao, comboStatusEdicao, filtroCategoria;
    public JButton btnCadastrar, btnEditar, btnExcluir, btnTrocarParaCadastrar, btnTrocarParaEditar;
    public JPanel painelCard;
    public CardLayout cardLayout;

    public TelaItem() {
        setTitle("Sistema de Gestão - Itens");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color vermelho = new Color(153, 0, 51);
        Font fonteBotao = new Font("Arial", Font.BOLD, 14);
        Color corTexto = Color.WHITE;

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

        JPanel painelTitulo = new JPanel();
        painelTitulo.setBackground(Color.LIGHT_GRAY);
        painelTitulo.setPreferredSize(new Dimension(0, 50));
        JLabel titulo = new JLabel("Cadastro de Itens");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        painelTitulo.add(titulo);

        btnTrocarParaCadastrar = new JButton("Cadastrar");
        btnTrocarParaEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        for (JButton b : new JButton[]{btnTrocarParaCadastrar, btnTrocarParaEditar, btnExcluir}) {
            b.setBackground(vermelho);
            b.setForeground(corTexto);
            b.setFont(fonteBotao);
            painelBotoes.add(b);
        }

        txtNomeCadastro = new JTextField();
        comboCategoriaCadastro = new JComboBox<>(new String[]{"Audio e Vídeo", "Informática", "Didático", "Laboratorial", "Chaves", "Esportivo", "Movéis", "Materiais", "Outros"});
        btnCadastrar = new JButton("Registrar Cadastro");
        btnCadastrar.setBackground(vermelho);
        btnCadastrar.setForeground(corTexto);
        btnCadastrar.setFont(fonteBotao);

        JPanel painelCadastro = new JPanel(new GridLayout(3, 2, 10, 10));
        painelCadastro.setBorder(BorderFactory.createTitledBorder("Cadastrar Item"));
        painelCadastro.add(new JLabel("Nome:"));
        painelCadastro.add(txtNomeCadastro);
        painelCadastro.add(new JLabel("Categoria:"));
        painelCadastro.add(comboCategoriaCadastro);
        painelCadastro.add(new JLabel());
        painelCadastro.add(btnCadastrar);

        txtNomeEdicao = new JTextField();
        comboCategoriaEdicao = new JComboBox<>(new String[]{"Audio e Vídeo", "Informática", "Didático", "Laboratorial", "Chaves", "Esportivo", "Movéis", "Materiais", "Outros"});
        comboStatusEdicao = new JComboBox<>(new String[]{"ativo", "inativo", "em manutenção"});
        btnEditar = new JButton("Registrar Edição");
        btnEditar.setBackground(vermelho);
        btnEditar.setForeground(corTexto);
        btnEditar.setFont(fonteBotao);

        JPanel painelEdicao = new JPanel(new GridLayout(4, 2, 10, 10));
        painelEdicao.setBorder(BorderFactory.createTitledBorder("Editar Item"));
        painelEdicao.add(new JLabel("Nome:"));
        painelEdicao.add(txtNomeEdicao);
        painelEdicao.add(new JLabel("Categoria:"));
        painelEdicao.add(comboCategoriaEdicao);
        painelEdicao.add(new JLabel("Status:"));
        painelEdicao.add(comboStatusEdicao);
        painelEdicao.add(new JLabel());
        painelEdicao.add(btnEditar);

        cardLayout = new CardLayout();
        painelCard = new JPanel(cardLayout);
        painelCard.add(painelCadastro, "cadastro");
        painelCard.add(painelEdicao, "edicao");

        btnTrocarParaCadastrar.addActionListener(e -> cardLayout.show(painelCard, "cadastro"));
        btnTrocarParaEditar.addActionListener(e -> cardLayout.show(painelCard, "edicao"));

        // Filtros e tabela
        campoBusca = new JTextField(20);
        filtroCategoria = new JComboBox<>(new String[]{"Todos", "Audio e Vídeo", "Informática", "Didático", "Laboratorial", "Chaves", "Esportivo", "Movéis", "Materiais", "Outros"});

        JPanel painelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelFiltros.add(new JLabel("Buscar Nome:"));
        painelFiltros.add(campoBusca);
        painelFiltros.add(new JLabel("Categoria:"));
        painelFiltros.add(filtroCategoria);

        modelo = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Nome", "Categoria", "Status"}) {
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

        // Painéis organizados
        JPanel painelTopoTudo = new JPanel(new BorderLayout());
        painelTopoTudo.add(painelTitulo, BorderLayout.NORTH);
        painelTopoTudo.add(painelBotoes, BorderLayout.CENTER);
        painelTopoTudo.add(painelCard, BorderLayout.SOUTH);

        JPanel painelSuperiorCompleto = new JPanel(new BorderLayout());
        painelSuperiorCompleto.add(painelTopoTudo, BorderLayout.NORTH);
        painelSuperiorCompleto.add(painelFiltros, BorderLayout.SOUTH);

        JPanel painelDireito = new JPanel(new BorderLayout());
        painelDireito.add(painelSuperiorCompleto, BorderLayout.NORTH);
        painelDireito.add(scrollTabela, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelLateral, painelDireito);
        splitPane.setDividerLocation(200);
        splitPane.setEnabled(false);
        add(splitPane);

        setVisible(true);
    }
}
