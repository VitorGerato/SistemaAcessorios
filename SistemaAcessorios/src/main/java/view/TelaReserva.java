package view;

import controller.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * TelaReserva:
 * Controle de reservas feitas e pedidos para realizar alguma reserva.
 */
public class TelaReserva extends JFrame {
    public JTable tabelaAceitas;
    public DefaultTableModel modeloAceitas;
    public JTextField filtroItem;
    public CardLayout cardLayout;
    public JPanel painelCard;
    public JButton btnPedidosReserva;
    public JButton btnFecharReserva;
    public JPanel painelCards;

    public TelaReserva() {
        setTitle("Sistema de Gestão - Reservas");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color vermelho = new Color(153, 0, 51);
        Font fonteBotao = new Font("Arial", Font.BOLD, 14);
        Color corTexto = Color.WHITE;

        // Painel lateral
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

            painelTopo.add(Box.createVerticalStrut(15)); // espaçamento vertical
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

        // Painel superior
        JPanel painelSuperior = new JPanel();
        painelSuperior.setBackground(Color.LIGHT_GRAY);
        painelSuperior.setPreferredSize(new Dimension(0, 50));
        JLabel titulo = new JLabel("Reservas");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        painelSuperior.add(titulo);

        // Painel filtros
        JPanel painelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtroItem = new JTextField(20);
        btnPedidosReserva = new JButton("Pedidos de Reserva");
        btnPedidosReserva.setBackground(vermelho);
        btnPedidosReserva.setForeground(corTexto);
        btnPedidosReserva.setFont(fonteBotao);
        btnFecharReserva = new JButton("Fechar Reserva");
        btnFecharReserva.setBackground(vermelho);
        btnFecharReserva.setForeground(corTexto);
        btnFecharReserva.setFont(fonteBotao);

        painelFiltros.add(new JLabel("Buscar Item:"));
        painelFiltros.add(filtroItem);
        painelFiltros.add(btnPedidosReserva);
        painelFiltros.add(btnFecharReserva);

        // Tabela de reservas aceitas
        modeloAceitas = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Item", "Funcionário", "Data Reserva", "Status"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaAceitas = new JTable(modeloAceitas);
        tabelaAceitas.setRowHeight(28);
        tabelaAceitas.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaAceitas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        JScrollPane scrollAceitas = new JScrollPane(tabelaAceitas);

        // Painel de cards (substitui tabela pendente)
        painelCards = new JPanel();
        painelCards.setLayout(new BoxLayout(painelCards, BoxLayout.Y_AXIS));
        JScrollPane scrollCards = new JScrollPane(painelCards);

        // Painel com CardLayout
        cardLayout = new CardLayout();
        painelCard = new JPanel(cardLayout);
        painelCard.add(scrollAceitas, "aceitas");
        painelCard.add(scrollCards, "pendentes");

        // Painel direito principal
        painelTopo = new JPanel(new BorderLayout());
        painelTopo.add(painelSuperior, BorderLayout.NORTH);
        painelTopo.add(painelFiltros, BorderLayout.CENTER);
        JPanel painelDireito = new JPanel(new BorderLayout());
        painelDireito.add(painelTopo, BorderLayout.NORTH);
        painelDireito.add(painelCard, BorderLayout.CENTER);

        // SplitPane entre lateral e conteúdo
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelLateral, painelDireito);
        splitPane.setDividerLocation(200);
        splitPane.setEnabled(false);

        add(splitPane);

        // Ação do botão
        btnPedidosReserva.addActionListener(e -> {
            if (btnPedidosReserva.getText().equals("Pedidos de Reserva")) {
                cardLayout.show(painelCard, "pendentes");
                btnPedidosReserva.setText("Voltar");
                btnFecharReserva.setVisible(false);
            } else {
                cardLayout.show(painelCard, "aceitas");
                btnPedidosReserva.setText("Pedidos de Reserva");
                btnFecharReserva.setVisible(true);
            }
        });

        setVisible(true);
    }
}