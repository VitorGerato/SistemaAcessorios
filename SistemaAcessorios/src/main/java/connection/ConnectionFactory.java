package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*ConnectionFactory:
* Conexão com o banco DB_Sistema
*/
public class ConnectionFactory {
    private static final String URL = "jdbc:mysql://localhost:3306/DB_Sistema";
    private static final String USER = "root";
    private static final String PASSWORD = "adm6402";

    public ConnectionFactory() {
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/DB_Sistema", "root", "adm6402");
        } catch (SQLException ex) {
            throw new RuntimeException("Erro na conexão com o banco de dados: ", ex);
        }
    }

}