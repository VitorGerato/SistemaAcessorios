package model.dao;

import connection.ConnectionFactory;
import model.bean.Administrador;
import model.bean.Emprestimo;
import model.bean.EmprestimoItem;

import javax.swing.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * DAO para manipulação da tabela de Empréstimos.
 * Inclui CRUD, relatórios e integração com reservas.
 */
public class EmprestimoDAO {

    //Insere um novo empréstimo no banco.
    public void inserirEmprestimo(Emprestimo e) {
        Connection conn = ConnectionFactory.getConnection();
        try {
            String sql = "INSERT INTO emprestimo (ID_Funcionario, ID_Admin, Data_Retirada, Data_Devolucao, Data_Prevista) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, e.getIdFuncionario());
            stmt.setInt(2, e.getIdAdmin());
            stmt.setDate(3, new java.sql.Date(e.getDataRetirada().getTime()));
            stmt.setDate(4, e.getDataDevolucao() != null ? new java.sql.Date(e.getDataDevolucao().getTime()) : null);
            stmt.setDate(5, e.getDataPrevista() != null ? new java.sql.Date(e.getDataPrevista().getTime()) : null);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idEmprestimoGerado = rs.getInt(1);

                String sqlItem = "INSERT INTO emprestimo_item (ID_Emprestimo, ID_Item, Status_Devolucao, Observacoes) VALUES (?, ?, ?, ?)";
                for (EmprestimoItem item : e.getItens()) {
                    PreparedStatement stmtItem = conn.prepareStatement(sqlItem);
                    stmtItem.setInt(1, idEmprestimoGerado);
                    stmtItem.setInt(2, item.getIdItem());
                    stmtItem.setString(3, item.getStatusDevolucao());
                    stmtItem.setString(4, item.getObservacoes());
                    stmtItem.executeUpdate();
                }
            }

            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //Lista todos os empréstimos.
    public List<Emprestimo> readComNomes() {
        List<Emprestimo> lista = new ArrayList<>();
        String sql = "SELECT e.ID_Emprestimo, e.ID_Funcionario, f.Nome AS NomeFuncionario, e.ID_Admin, a.Nome AS NomeAdmin, " +
                "e.Data_Retirada, e.Data_Devolucao, e.Data_Prevista " +
                "FROM emprestimo e " +
                "JOIN funcionario f ON e.ID_Funcionario = f.ID_Funcionario " +
                "JOIN administrador a ON e.ID_Admin = a.ID_Admin";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Emprestimo emp = new Emprestimo();
                emp.setId(rs.getInt("ID_Emprestimo"));
                emp.setIdFuncionario(rs.getInt("ID_Funcionario"));
                emp.setDataRetirada(rs.getDate("Data_Retirada"));
                emp.setDataDevolucao(rs.getDate("Data_Devolucao"));
                emp.setDataPrevista(rs.getDate("Data_Prevista"));

                // Preenche dados do funcionário
                emp.setNomeFuncionario(rs.getString("NomeFuncionario"));

                // Preenche dados do admin
                Administrador admin = new Administrador();
                admin.setId(rs.getInt("ID_Admin"));
                admin.setNome(rs.getString("NomeAdmin"));
                emp.setAdministrador(admin);

                // Buscar itens associados
                List<EmprestimoItem> itens = new ArrayList<>();
                String sqlItens = "SELECT * FROM emprestimo_item WHERE ID_Emprestimo = ?";
                try (PreparedStatement stmtItens = conn.prepareStatement(sqlItens)) {
                    stmtItens.setInt(1, emp.getId());
                    ResultSet rsItens = stmtItens.executeQuery();
                    while (rsItens.next()) {
                        EmprestimoItem item = new EmprestimoItem();
                        item.setIdEmprestimoItem(rsItens.getInt("ID_Emprestimo_Item"));
                        item.setIdEmprestimo(emp.getId());
                        item.setIdItem(rsItens.getInt("ID_Item"));
                        item.setStatusDevolucao(rsItens.getString("Status_Devolucao"));
                        item.setObservacoes(rsItens.getString("Observacoes"));
                        itens.add(item);
                    }
                }

                emp.setItens(itens);
                lista.add(emp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Atualiza o empréstimo com a data de devolução e observações caso existam.
    public void marcarComoDevolvido(int idEmprestimoItem, Date dataDevolucao, String observacoes) {
        String sql = "UPDATE emprestimo_item SET Status_Devolucao = ?, Observacoes = ? WHERE ID_Emprestimo_Item = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "devolvido");
            stmt.setString(2, observacoes);
            stmt.setInt(3, idEmprestimoItem);

            stmt.executeUpdate();

            // Atualiza também a data de devolução no cabeçalho
            String sql2 = "UPDATE emprestimo SET Data_Devolucao = ? WHERE ID_Emprestimo = (SELECT ID_Emprestimo FROM emprestimo_item WHERE ID_Emprestimo_Item = ?)";
            try (PreparedStatement stmt2 = conn.prepareStatement(sql2)) {
                stmt2.setDate(1, dataDevolucao);
                stmt2.setInt(2, idEmprestimoItem);
                stmt2.executeUpdate();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao registrar devolução: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Retorna os itens mais utilizados com suas quantidades
    public Map<String, Integer> getItensMaisUtilizados() {
        Map<String, Integer> mapa = new LinkedHashMap<>();
        String sql = "SELECT i.Nome, COUNT(*) as Total " +
                "FROM emprestimo_item ei " +
                "JOIN item i ON ei.ID_Item = i.ID_Item " +
                "GROUP BY i.Nome ORDER BY Total DESC";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                mapa.put(rs.getString("Nome"), rs.getInt("Total"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return mapa;
    }

    // Retorna os funcionários que mais utilizaram com suas quantidades
    public Map<String, Integer> getFuncionariosMaisAtivos() {
        Map<String, Integer> mapa = new LinkedHashMap<>();
        String sql = "SELECT f.Nome, COUNT(*) as Total FROM Emprestimo e JOIN Funcionario f ON e.ID_Funcionario = f.ID_Funcionario GROUP BY f.Nome ORDER BY Total DESC";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                mapa.put(rs.getString("Nome"), rs.getInt("Total"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return mapa;
    }

    // Retorna lista de empréstimos em aberto
    public List<Emprestimo> getEmprestimosEmAberto() {
        List<Emprestimo> lista = new ArrayList<>();
        String sql = "SELECT e.ID_Emprestimo, e.ID_Funcionario, e.Data_Retirada, e.Data_Prevista, " +
                "ei.ID_Emprestimo_Item, ei.ID_Item, ei.Status_Devolucao, ei.Observacoes " +
                "FROM emprestimo e " +
                "JOIN emprestimo_item ei ON e.ID_Emprestimo = ei.ID_Emprestimo " +
                "WHERE ei.Status_Devolucao = 'pendente'";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Mapa para evitar duplicatas e agrupar itens por empréstimo
            Map<Integer, Emprestimo> mapa = new HashMap<>();

            while (rs.next()) {
                int idEmprestimo = rs.getInt("ID_Emprestimo");
                Emprestimo e;

                if (!mapa.containsKey(idEmprestimo)) {
                    e = new Emprestimo();
                    e.setId(idEmprestimo);
                    e.setIdFuncionario(rs.getInt("ID_Funcionario"));
                    e.setDataRetirada(rs.getDate("Data_Retirada"));
                    e.setDataPrevista(rs.getDate("Data_Prevista"));
                    e.setItens(new ArrayList<>());
                    mapa.put(idEmprestimo, e);
                } else {
                    e = mapa.get(idEmprestimo);
                }

                EmprestimoItem item = new EmprestimoItem();
                item.setIdEmprestimoItem(rs.getInt("ID_Emprestimo_Item"));
                item.setIdItem(rs.getInt("ID_Item"));
                item.setStatusDevolucao(rs.getString("Status_Devolucao"));
                item.setObservacoes(rs.getString("Observacoes"));

                e.getItens().add(item);
            }

            lista.addAll(mapa.values());

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return lista;
    }

    //verifica se o funcionario possuiu um emprestimo em aberto no sistema.
    public boolean FuncionarioEmprestimoAberto(int idFuncionario) {
        String sql = "SELECT ei.* FROM emprestimo e " +
                "JOIN emprestimo_item ei ON e.ID_Emprestimo = ei.ID_Emprestimo " +
                "WHERE e.ID_Funcionario = ? AND ei.Status_Devolucao = 'pendente'";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idFuncionario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int total = rs.getInt(1);
                return total > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public String buscarNomeItem(int idItem) {
        String nome = "";
        String sql = "SELECT Nome FROM item WHERE ID_Item = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idItem);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                nome = rs.getString("Nome");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nome;
    }

    // Verifica itens que não foram devolvidos
    public boolean itemEstaEmprestado(int idItem) {
        String sql = "SELECT 1 FROM emprestimo_item WHERE ID_Item = ? AND Status_Devolucao = 'pendente'";
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idItem);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}