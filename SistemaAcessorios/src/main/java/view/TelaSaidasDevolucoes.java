package view;

import com.toedter.calendar.JDateChooser;
import controller.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * TelaSaidasDevolucoes:
 * Registrar saídas e devoluções de itens.
 */
public class TelaSaidasDevolucoes extends JFrame {

    // Componentes visuais principais
    public JTable tabela;
    public DefaultTableModel modelo;

    // Campos do formulário de saída
    public JTextField txtIdFuncionario;
    public JTextField txtIdItem;
    public JTextField txtObservacoes;
    public JDateChooser dateChooserPrevista;
    public JButton btnRegistrarSaida;

    // Botão de devolução
    public JButton btnRegistrarDevolucao;

    // Filtros de tabela
    public JTextField filtroItem;
    public JComboBox<String> filtroStatus;

    // Layout alternável entre saída e devolução
    public CardLayout cardLayout;
    public JPanel painelCard;

    public TelaSaidasDevolucoes() {
        setTitle("Sistema de Gestão - Saídas e Devoluções");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Cores e fontes padrão
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

        // Painel superior com o título da tela
        JPanel painelSuperior = new JPanel();
        painelSuperior.setBackground(Color.LIGHT_GRAY);
        painelSuperior.setPreferredSize(new Dimension(0, 50));
        JLabel titulo = new JLabel("Saídas e Devoluções");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        painelSuperior.add(titulo);

        // Painel de filtros de busca e status
        JPanel painelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtroItem = new JTextField(20);
        filtroStatus = new JComboBox<>(new String[]{"Todos", "pendente", "devolvido"});
        painelFiltros.add(new JLabel("Buscar Item:"));
        painelFiltros.add(filtroItem);
        painelFiltros.add(new JLabel("Status:"));
        painelFiltros.add(filtroStatus);

        // Botões de alternância entre modo saída e devolução
        JPanel painelOpcoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnMostrarSaida = new JButton("Saída");
        JButton btnMostrarDevolucao = new JButton("Devolução");

        for (JButton botao : new JButton[]{btnMostrarSaida, btnMostrarDevolucao}) {
            botao.setBackground(vermelho);
            botao.setForeground(corTexto);
            botao.setFont(fonteBotao);
            painelOpcoes.add(botao);
        }

        // CardLayout: painel com dois formulários alternáveis
        cardLayout = new CardLayout();
        painelCard = new JPanel(cardLayout);

        // Formulário de saída
        JPanel painelSaida = new JPanel(new GridLayout(5, 2, 10, 10));
        painelSaida.setBorder(BorderFactory.createTitledBorder("Registrar Saída"));

        txtIdFuncionario = new JTextField();
        txtIdItem = new JTextField();
        txtObservacoes = new JTextField();
        dateChooserPrevista = new JDateChooser();

        btnRegistrarSaida = new JButton("Registrar Saída");
        btnRegistrarSaida.setPreferredSize(new Dimension(200, 40));
        btnRegistrarSaida.setBackground(vermelho);
        btnRegistrarSaida.setForeground(corTexto);
        btnRegistrarSaida.setFont(fonteBotao);

        painelSaida.add(new JLabel("ID Funcionário:"));
        painelSaida.add(txtIdFuncionario);
        painelSaida.add(new JLabel("ID Item:"));
        painelSaida.add(txtIdItem);
        painelSaida.add(new JLabel("Data Prevista para Devolução:"));
        painelSaida.add(dateChooserPrevista);
        painelSaida.add(new JLabel("Observações:"));
        painelSaida.add(txtObservacoes);
        painelSaida.add(new JLabel());
        painelSaida.add(btnRegistrarSaida);

        // Formulário de devolução
        JPanel painelDevolucao = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 15));
        painelDevolucao.setBorder(BorderFactory.createTitledBorder("Registrar Devolução"));

        JLabel labelInfo = new JLabel("Para devolver, selecione um item da tabela abaixo");
        painelDevolucao.add(labelInfo);

        btnRegistrarDevolucao = new JButton("Registrar Devolução");
        btnRegistrarDevolucao.setPreferredSize(new Dimension(200, 35));
        btnRegistrarDevolucao.setBackground(vermelho);
        btnRegistrarDevolucao.setForeground(corTexto);
        btnRegistrarDevolucao.setFont(fonteBotao);

        painelDevolucao.add(Box.createVerticalStrut(10));
        painelDevolucao.add(btnRegistrarDevolucao);

        // Adiciona os dois cards ao painel alternável
        painelCard.add(painelSaida, "saida");
        painelCard.add(painelDevolucao, "devolucao");

        // Alternância entre os painéis
        btnMostrarSaida.addActionListener(e -> cardLayout.show(painelCard, "saida"));
        btnMostrarDevolucao.addActionListener(e -> cardLayout.show(painelCard, "devolucao"));

        // Modelo e tabela
        modelo = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Funcionário", "Item", "Administrador", "Data Retirada", "Data Prevista", "Data Devolução", "Status", "Observações"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modelo);

        // formatação das cores na tabela para as datas de devolução
        tabela.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String nomeColuna = table.getColumnName(column);

                if ("Data Prevista".equalsIgnoreCase(nomeColuna) && value != null) {
                    try {
                        java.util.Date dataPrevista;

                        if (value instanceof java.util.Date) {
                            dataPrevista = (java.util.Date) value;
                        } else {
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                            dataPrevista = sdf.parse(value.toString());
                        }

                        // zerar a hora para comparar apenas a data
                        java.util.Calendar hoje = java.util.Calendar.getInstance();
                        hoje.set(java.util.Calendar.HOUR_OF_DAY, 0);
                        hoje.set(java.util.Calendar.MINUTE, 0);
                        hoje.set(java.util.Calendar.SECOND, 0);
                        hoje.set(java.util.Calendar.MILLISECOND, 0);

                        java.util.Calendar prevista = java.util.Calendar.getInstance();
                        prevista.setTime(dataPrevista);
                        prevista.set(java.util.Calendar.HOUR_OF_DAY, 0);
                        prevista.set(java.util.Calendar.MINUTE, 0);
                        prevista.set(java.util.Calendar.SECOND, 0);
                        prevista.set(java.util.Calendar.MILLISECOND, 0);

                        long diffDias = (prevista.getTimeInMillis() - hoje.getTimeInMillis()) / (1000 * 60 * 60 * 24);

                        if (diffDias < 0) {
                            c.setBackground(new Color(255, 102, 102));
                        } else if (diffDias == 0) {
                            c.setBackground(new Color(253, 253, 127));
                        } else {
                            c.setBackground(new Color(124, 255, 124));
                        }
                    } catch (Exception ex) {
                        c.setBackground(Color.WHITE);
                    }
                } else {
                    c.setBackground(Color.WHITE);
                }

                return c;
            }
        });

        tabela.setRowHeight(28);
        tabela.setFont(new Font("Arial", Font.PLAIN, 14));
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        JScrollPane scrollTabela = new JScrollPane(tabela);

        // Painel principal de conteúdo
        JPanel painelConteudo = new JPanel(new BorderLayout());
        painelConteudo.add(painelSuperior, BorderLayout.NORTH);
        painelConteudo.add(painelOpcoes, BorderLayout.CENTER);
        painelConteudo.add(painelCard, BorderLayout.SOUTH);

        // Painel inferior com filtros e tabela
        painelInferior = new JPanel(new BorderLayout());
        painelInferior.add(painelFiltros, BorderLayout.NORTH);
        painelInferior.add(scrollTabela, BorderLayout.CENTER);

        // Painel da parte direita (conteúdo geral)
        JPanel painelDireito = new JPanel(new BorderLayout());
        painelDireito.add(painelConteudo, BorderLayout.NORTH);
        painelDireito.add(painelInferior, BorderLayout.CENTER);

        // Divide menu lateral e conteúdo
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelLateral, painelDireito);
        splitPane.setDividerLocation(200);
        splitPane.setEnabled(false);

        add(splitPane);
        setVisible(true);
    }
}