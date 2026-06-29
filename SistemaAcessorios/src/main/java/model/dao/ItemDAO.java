package model.dao;

import connection.ConnectionFactory;
import model.bean.Item;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para manipulação da tabela de Empréstimos.
 * Inclui integração com Item.
 */
public class ItemDAO {

    //Insere um novo item no banco.
    public void create(Item item) {
        String sql = "INSERT INTO Item (Nome, Categoria, Status_Item) VALUES (?, ?, ?)";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, item.getNome());
            stmt.setString(2, item.getCategoria());
            stmt.setString(3, item.getStatus());
            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //Retorna todos os itens que não estão em manutenção.
    public List<Item> read() {
        List<Item> itens = new ArrayList<>();
        String sql = "SELECT * FROM Item WHERE ID_Item NOT IN (SELECT ID_Item FROM Manutencao)";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getInt("ID_Item"));
                item.setNome(rs.getString("Nome"));
                item.setCategoria(rs.getString("Categoria"));
                item.setStatus(rs.getString("Status_Item"));
                itens.add(item);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return itens;
    }

    //Atualiza os dados de um item existente.
    public void update(Item item) {
        String sql = "UPDATE Item SET Nome = ?, Categoria = ?, Status_Item = ? WHERE ID_Item = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, item.getNome());
            stmt.setString(2, item.getCategoria());
            stmt.setString(3, item.getStatus());
            stmt.setInt(4, item.getId());
            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Remove um item pelo seu ID.
    public void delete(int id) {
        String sql = "DELETE FROM Item WHERE ID_Item = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Atualiza apenas o status de um item.
    public void updateStatus(int idItem, String status) {
        String sql = "UPDATE Item SET Status_Item = ? WHERE ID_Item = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, idItem);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Retorna o status atual do item com base no ID.
    public String getStatusDoItem(int idItem) {
        String status = "";
        String sql = "SELECT Status_Item FROM Item WHERE ID_Item = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idItem);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                status = rs.getString("Status_Item");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return status;
    }

    // Verifica item existente pelo ID
    public boolean existeItem(int id) {
        String sql = "SELECT 1 FROM item WHERE ID_Item = ?";
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

    public boolean podeExcluirItem(int idItem) {
        Connection conn = ConnectionFactory.getConnection();
        try {
            // Verifica se o item está em manutenção
            PreparedStatement stmt1 = conn.prepareStatement("SELECT COUNT(*) FROM manutencao WHERE ID_Item = ?");
            stmt1.setInt(1, idItem);
            ResultSet rs1 = stmt1.executeQuery();
            if (rs1.next() && rs1.getInt(1) > 0) return false;

            // Verifica se o item está em reserva pendente ou aceita
            PreparedStatement stmt2 = conn.prepareStatement("SELECT COUNT(*) FROM reserva WHERE ID_Item = ? AND Status_Reserva IN ('pendente', 'aceita')");
            stmt2.setInt(1, idItem);
            ResultSet rs2 = stmt2.executeQuery();
            if (rs2.next() && rs2.getInt(1) > 0) return false;

            // Verifica se o item está em empréstimo em aberto
            PreparedStatement stmt3 = conn.prepareStatement(
                    "SELECT COUNT(*) FROM emprestimo_item ei " +
                            "JOIN emprestimo e ON ei.ID_Emprestimo = e.ID_Emprestimo " +
                            "WHERE ei.ID_Item = ? AND ei.Status_Devolucao != 'devolvido'"
            );
            stmt3.setInt(1, idItem);
            ResultSet rs3 = stmt3.executeQuery();
            if (rs3.next() && rs3.getInt(1) > 0) return false;

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void excluirItemComDependencias(int idItem) {
        if (!podeExcluirItem(idItem)) {
            JOptionPane.showMessageDialog(null, "Este item está vinculado a uma reserva, manutenção ou empréstimo em aberto.");
            return;
        }

        Connection conn = ConnectionFactory.getConnection();
        try {
            // Excluir registros dependentes (reserva, manutenção, empréstimo_item)
            PreparedStatement delReservas = conn.prepareStatement("DELETE FROM reserva WHERE ID_Item = ?");
            delReservas.setInt(1, idItem);
            delReservas.executeUpdate();

            PreparedStatement delManutencao = conn.prepareStatement("DELETE FROM manutencao WHERE ID_Item = ?");
            delManutencao.setInt(1, idItem);
            delManutencao.executeUpdate();

            PreparedStatement delEmprestimos = conn.prepareStatement("DELETE FROM emprestimo_item WHERE ID_Item = ?");
            delEmprestimos.setInt(1, idItem);
            delEmprestimos.executeUpdate();

            // Excluir o item
            PreparedStatement delItem = conn.prepareStatement("DELETE FROM item WHERE ID_Item = ?");
            delItem.setInt(1, idItem);
            delItem.executeUpdate();

            JOptionPane.showMessageDialog(null, "Item excluído com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao excluir item.");
        }
    }
}