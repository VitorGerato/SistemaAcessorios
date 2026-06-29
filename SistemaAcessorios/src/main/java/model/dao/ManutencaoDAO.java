package model.dao;

import connection.ConnectionFactory;
import model.bean.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para manipulação da tabela de Empréstimos.
 * Inclui integração com a tabela da manutenção.
 */
public class ManutencaoDAO {

    // Retorna todos os itens em manutenção.
    public List<Item> readManutencao() {
        List<Item> lista = new ArrayList<>();

        String sql = "SELECT m.ID_Item, i.Nome, i.Categoria, i.Status_Item, m.Data_Entrada, m.Observacoes " +
                "FROM Manutencao m JOIN Item i ON m.ID_Item = i.ID_Item";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getInt("ID_Item"));
                item.setNome(rs.getString("Nome"));
                item.setCategoria(rs.getString("Categoria"));
                item.setStatus(rs.getString("Status_Item"));
                item.setDataEntrada(rs.getDate("Data_Entrada"));
                item.setObservacoes(rs.getString("Observacoes"));
                lista.add(item);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return lista;
    }

    /**
     * Insere um item na tabela de manutenção.
     * Recebe o ID, data de entrada e observações.
     */

    // Envia o item para a manutenção com a mudança de status
    public void enviarParaManutencao(int idItem, Date dataEntrada, String observacoes) {
        try (Connection con = ConnectionFactory.getConnection()) {
            String updateItem = "UPDATE item SET Status_Item = 'em manutenção' WHERE ID_Item = ?";
            try (PreparedStatement stmt = con.prepareStatement(updateItem)) {
                stmt.setInt(1, idItem);
                stmt.executeUpdate();
            }

            String insertManutencao = "INSERT INTO manutencao (ID_Item, Data_Entrada, Observacoes) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(insertManutencao)) {
                stmt.setInt(1, idItem);
                stmt.setDate(2, new java.sql.Date(dataEntrada.getTime()));
                stmt.setString(3, observacoes);
                stmt.executeUpdate();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Remove o item da manutenção e retorna a tabela em "Itens"
    public void removerRegistro(int idItem) {
        try (Connection con = ConnectionFactory.getConnection()) {
            String updateItem = "UPDATE item SET Status_Item = 'ativo' WHERE ID_Item = ?";
            try (PreparedStatement stmt = con.prepareStatement(updateItem)) {
                stmt.setInt(1, idItem);
                stmt.executeUpdate();
            }

            String deleteManutencao = "DELETE FROM manutencao WHERE ID_Item = ?";
            try (PreparedStatement stmt = con.prepareStatement(deleteManutencao)) {
                stmt.setInt(1, idItem);
                stmt.executeUpdate();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
