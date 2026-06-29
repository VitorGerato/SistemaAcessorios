package view;

import controller.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * TelaManutencao:
 * Exibir itens em manutenção.
 */
public class TelaManutencao extends JFrame {

    public JTable tabela;
    public DefaultTableModel modelo;
    public JTextField filtroNome;
    public JButton btnDevolver;

    public TelaManutencao() {
        setTitle("Sistema de Gestão - Manutenção");
        setSize(1250, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color vermelho = new Color(153, 0, 51);
        Font fonteBotao = new Font("Arial", Font.BOLD, 14);
        Color corTexto = Color.WHITE;

        // Painel lateral com botões de navegação
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

        // Painel de título superior
        JPanel painelTitulo = new JPanel();
        painelTitulo.setBackground(Color.LIGHT_GRAY);
        painelTitulo.setPreferredSize(new Dimension(0, 50));
        JLabel titulo = new JLabel("Manutenção de Itens");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        painelTitulo.add(titulo);

        // Painel de filtros (nome e status)
        JPanel painelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtroNome = new JTextField(15);

        painelFiltros.add(new JLabel("Buscar por nome:"));
        painelFiltros.add(filtroNome);

        // Botão de devolução
        btnDevolver = new JButton("Devolver ao Estoque");
        btnDevolver.setBackground(vermelho);
        btnDevolver.setForeground(corTexto);
        btnDevolver.setFont(fonteBotao);
        painelFiltros.add(btnDevolver);

        // Modelo e tabela de visualização
        modelo = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Nome", "Categoria", "Status", "Data Entrada", "Observações"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modelo);
        tabela.setRowHeight(30);
        tabela.setFont(new Font("Arial", Font.PLAIN, 14));
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        JScrollPane scrollTabela = new JScrollPane(tabela);

        // Montagem do conteúdo principal
        JPanel painelDireito = new JPanel(new BorderLayout());
        painelTopo = new JPanel(new BorderLayout());
        painelTopo.add(painelTitulo, BorderLayout.NORTH);
        painelTopo.add(painelFiltros, BorderLayout.CENTER);
        painelDireito.add(painelTopo, BorderLayout.NORTH);
        painelDireito.add(scrollTabela, BorderLayout.CENTER);

        // SplitPane entre menu e conteúdo
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelLateral, painelDireito);
        splitPane.setDividerLocation(200);
        splitPane.setEnabled(false);

        add(splitPane);
        setVisible(true);
    }
}