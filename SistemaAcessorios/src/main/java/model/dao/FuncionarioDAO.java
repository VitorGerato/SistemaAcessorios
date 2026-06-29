package model.dao;

import connection.ConnectionFactory;
import model.bean.Funcionario;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para manipulação da tabela de Empréstimos.
 * Inclui CRUD com a tabela Funcionario.
 */

public class FuncionarioDAO {

    //Insere um novo funcionário no banco.
    public void create(Funcionario f) {
        String sql = "INSERT INTO Funcionario (Nome, RE, Status_Func, Cargo, Senha) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, f.getNome());
            stmt.setString(2, f.getRe());
            stmt.setString(3, f.getStatus());
            stmt.setString(4, f.getCargo());
            stmt.setString(5, f.getSenha());
            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //Retorna todos os funcionários cadastrados.

    public List<Funcionario> read() {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT * FROM Funcionario";

        try (Connection con = ConnectionFactory.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Funcionario f = new Funcionario();
                f.setId(rs.getInt("ID_Funcionario"));
                f.setNome(rs.getString("Nome"));
                f.setRe(rs.getString("RE"));
                f.setStatus(rs.getString("Status_Func"));
                f.setCargo(rs.getString("Cargo"));
                f.setSenha(rs.getString("Senha"));
                funcionarios.add(f);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return funcionarios;
    }

    //Atualiza os dados de um funcionário existente.
    public void update(Funcionario f) {
        String sql = "UPDATE Funcionario SET Nome = ?, RE = ?, Status_Func = ?, Cargo = ?, Senha = ? WHERE ID_Funcionario = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, f.getNome());
            stmt.setString(2, f.getRe());
            stmt.setString(3, f.getStatus());
            stmt.setString(4, f.getCargo());
            stmt.setString(5, f.getSenha());
            stmt.setInt(6, f.getId());
            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Exclui um funcionário pelo ID.
    public void delete(int id) {
        String sql = "DELETE FROM Funcionario WHERE ID_Funcionario = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Confere para que não haja cadastros com o mesmo RE
    public boolean reExiste(String re, Integer ignorarId) {
        String sql = "SELECT COUNT(*) FROM funcionario WHERE RE = ?";
        if (ignorarId != null) {
            sql += " AND ID_Funcionario <> ?";
        }

        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, re);

            if (ignorarId != null) {
                stmt.setInt(2, ignorarId);
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Validação para verificar ID existente ou não
    public boolean existeFuncionario(int id) {
        String sql = "SELECT 1 FROM funcionario WHERE ID_Funcionario = ?";
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Verifica status do funcionario
    public String getStatusFuncionario(int id) {
        String sql = "SELECT Status_Func FROM funcionario WHERE ID_Funcionario = ?";
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Status_Func");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

