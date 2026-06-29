package view;

import controller.*;

import javax.swing.*;
import java.awt.*;

/**
 * TelaRelatorio:
 * Tela gráfica para exportar relatórios em formato CSV.
 */
public class TelaRelatorio extends JFrame {

    public JButton btnExportar;

    public TelaRelatorio() {
        setTitle("Sistema de Gestão - Relatórios");
        setSize(1200, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Cores e fonte padrão
        Color vermelho = new Color(153, 0, 51);
        Font fonteBotao = new Font("Arial", Font.BOLD, 14);
        Color corTexto = Color.WHITE;

        // Painel lateral (menu)
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
        Dimension tamanhoBotao = new Dimension(180, 45); // Tamanho original

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

        // Título superior
        JPanel painelTitulo = new JPanel();
        painelTitulo.setBackground(Color.LIGHT_GRAY);
        painelTitulo.setPreferredSize(new Dimension(0, 50));
        JLabel titulo = new JLabel("Exportação de Relatórios");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        painelTitulo.add(titulo);

        // Botão de exportação
        btnExportar = new JButton("Exportar CSV");
        btnExportar.setBackground(vermelho);
        btnExportar.setForeground(corTexto);
        btnExportar.setFont(fonteBotao);
        btnExportar.setPreferredSize(new Dimension(200, 40));

        // Painel central com card visual no topo e botão centralizado
        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(new BoxLayout(painelCentral, BoxLayout.Y_AXIS));
        painelCentral.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        // Card com título e texto informativo
        JPanel cardInfo = new JPanel();
        cardInfo.setLayout(new BorderLayout());
        cardInfo.setBorder(BorderFactory.createTitledBorder("Gerar Relatório CSV"));

        JLabel lblDescricao = new JLabel("<html><div style='text-align: center;'>"
                + "Este relatório irá incluir:<br>"
                + "- Itens mais utilizados<br>"
                + "- Funcionários mais ativos<br>"
                + "- Empréstimos em aberto</div></html>", SwingConstants.CENTER);
        lblDescricao.setFont(new Font("Arial", Font.PLAIN, 14));
        cardInfo.add(lblDescricao, BorderLayout.CENTER);

        painelCentral.add(cardInfo);
        painelCentral.add(Box.createVerticalStrut(30));

        // Painel do botão
        JPanel painelBotao = new JPanel();
        painelBotao.add(btnExportar);
        painelCentral.add(painelBotao);

        // Montagem final da interface
        JPanel painelDireito = new JPanel(new BorderLayout());
        painelDireito.add(painelTitulo, BorderLayout.NORTH);
        painelDireito.add(painelCentral, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelLateral, painelDireito);
        splitPane.setDividerLocation(200);
        splitPane.setEnabled(false);

        add(splitPane);
        setVisible(true);
    }
}