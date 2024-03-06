package utils.bbdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static utils.bbdd.ConstanstBD.*;

public class ConexionBD {

    private String url = URL_DB_liquidador_qa;
    private String user = USER_BD_liquidador_qa;
    private String password =PASS_BD_liquidador_qa;

    public ConexionBD(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.url, this.user, this.password);
    }

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Coneccion establecida con PostgreSQL");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public static void close(ResultSet rs) {
        try {
            rs.close();
        } catch (SQLException var2) {
            var2.printStackTrace(System.out);
        }

    }

    public static void close(Connection conn) {
        try {
            conn.close();
        } catch (SQLException var2) {
            var2.printStackTrace(System.out);
        }

    }

    public static void close(PreparedStatement ps) {
        try {
            ps.close();
        } catch (SQLException var2) {
            var2.printStackTrace(System.out);
        }

    }

}
