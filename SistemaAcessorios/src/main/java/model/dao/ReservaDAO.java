package model.dao;

import connection.ConnectionFactory;
import model.bean.Reserva;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    public List<Reserva> readComNomes() {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT r.ID_Reserva, r.ID_Funcionario, r.ID_Item, r.Data_Reserva, r.Status_Reserva, r.Observacoes, " +
                "f.Nome AS NomeFuncionario, i.Nome AS NomeItem " +
                "FROM Reserva r JOIN Funcionario f ON r.ID_Funcionario = f.ID_Funcionario " +
                "JOIN Item i ON r.ID_Item = i.ID_Item";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Reserva r = new Reserva();
                r.setId(rs.getInt("ID_Reserva"));
                r.setIdFuncionario(rs.getInt("ID_Funcionario"));
                r.setIdItem(rs.getInt("ID_Item"));
                r.setDataReserva(rs.getDate("Data_Reserva"));
                r.setStatus(rs.getString("Status_Reserva"));
                r.setObservacoes(rs.getString("Observacoes"));
                r.setNomeFuncionario(rs.getString("NomeFuncionario"));
                r.setNomeItem(rs.getString("NomeItem"));
                lista.add(r);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return lista;
    }

    public void aceitar(int id) {
        String sql = "UPDATE Reserva SET Status_Reserva = 'aceita' WHERE ID_Reserva = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Atualiza o status da reserva para recusado com a justificativa.
    public void recusar(int idReserva, String observacao) {
        String sql = "UPDATE Reserva SET Status_Reserva = 'recusado', Observacoes = ? WHERE ID_Reserva = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, observacao);
            stmt.setInt(2, idReserva);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //Fecha uma reserva ja aceita na tabela principal da TelaReserva
    public boolean fechar(int id) {
        String sql = "UPDATE reserva SET Status_Reserva = 'fechada' WHERE ID_Reserva = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}