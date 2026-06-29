package view;

import connection.ConnectionFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.bean.Administrador;
import model.bean.Sessao;

/**
 * TelaLogin: Tela inicial de autenticação do sistema.
 * Permite que um administrador entre usando RE e senha.
 */
public class TelaLogin extends JFrame {

    private JTextField txtRE;
    private JPasswordField txtSenha;
    private JButton btnEntrar;

    public TelaLogin() {
        // Janela principal
        setTitle("Login");
        setSize(420, 420);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(230, 230, 230));
        setLayout(new BorderLayout());

        JPanel loginBox = new JPanel();
        loginBox.setPreferredSize(new Dimension(350, 300));
        loginBox.setBackground(Color.WHITE);
        loginBox.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        loginBox.setLayout(new BoxLayout(loginBox, BoxLayout.Y_AXIS));
        loginBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = new JLabel("Login");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        txtRE = new JTextField();
        txtRE.setMaximumSize(new Dimension(300, 45));
        txtRE.setFont(new Font("Arial", Font.PLAIN, 16));
        txtRE.setMargin(new Insets(10, 10, 10, 10));
        txtRE.setBorder(BorderFactory.createTitledBorder("Registro de Empregado"));

        txtSenha = new JPasswordField();
        txtSenha.setMaximumSize(new Dimension(300, 45));
        txtSenha.setFont(new Font("Arial", Font.PLAIN, 16));
        txtSenha.setMargin(new Insets(10, 10, 10, 10));
        txtSenha.setBorder(BorderFactory.createTitledBorder("Senha"));

        btnEntrar = new JButton("Acessar");
        btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEntrar.setPreferredSize(new Dimension(120, 40));
        btnEntrar.setBackground(new Color(153, 0, 51));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 16));
        btnEntrar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnEntrar.addActionListener(this::verificarLogin);

        // Link da "Esqueci minha senha"
        JLabel esqueceuSenha = new JLabel("<HTML><U>Esqueci minha senha</U></HTML>");
        esqueceuSenha.setForeground(Color.BLUE.darker());
        esqueceuSenha.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        esqueceuSenha.setAlignmentX(Component.CENTER_ALIGNMENT);
        esqueceuSenha.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        esqueceuSenha.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(null,
                        "Contate o suporte para redefinir sua senha:\nEmail: suporteagv@gmail.com\nTelefone: (19) 99988-1122",
                        "Recuperar Senha", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Adiciona os componentes à tela
        loginBox.add(Box.createVerticalGlue());

        loginBox.add(titulo);
        loginBox.add(Box.createVerticalStrut(10));

        loginBox.add(txtRE);
        loginBox.add(Box.createVerticalStrut(10));

        loginBox.add(txtSenha);
        loginBox.add(Box.createVerticalStrut(20));

        loginBox.add(btnEntrar);
        loginBox.add(Box.createVerticalStrut(10));

        esqueceuSenha.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel painelSenha = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        painelSenha.setOpaque(false);
        painelSenha.add(esqueceuSenha);
        loginBox.add(painelSenha);

        loginBox.add(Box.createVerticalGlue());

        add(Box.createVerticalGlue(), BorderLayout.NORTH);
        add(loginBox, BorderLayout.CENTER);
        add(Box.createVerticalGlue(), BorderLayout.SOUTH);

        setVisible(true);
    }

    //verificarLogin: Autentica o usuário com base no RE e senha.
    private void verificarLogin(ActionEvent e) {
        String re = txtRE.getText().trim();
        String senha = new String(txtSenha.getPassword());

        try (Connection con = ConnectionFactory.getConnection()) {
            String sql = "SELECT * FROM Administrador WHERE RE = ? AND Senha = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, re);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Login bem-sucedido = abre a tela principal
                Administrador admin = new Administrador();
                admin.setId(rs.getInt("ID_Admin"));
                admin.setNome(rs.getString("Nome"));
                admin.setCargo(rs.getString("Cargo"));
                admin.setSenha(rs.getString("Senha"));

                Sessao.setAdministrador(admin);

                dispose();
                TelaFuncionario tela = new TelaFuncionario();
                new controller.FuncionarioController(tela);
            } else {
                JOptionPane.showMessageDialog(this, "Usuário ou senha incorretos.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar: " + ex.getMessage());
        }
    }

    // metodo main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TelaLogin::new);
    }
}