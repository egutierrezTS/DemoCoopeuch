package utils.bbdd;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static utils.bbdd.ConstanstBD.*;

public class Consultas {

    private Connection conn;


    public void ejemmploConsulta(String parametro){
        Connection conexion = null;
        ConexionBD conn = new ConexionBD(URL_DB_liquidador_qa, USER_BD_liquidador_qa, PASS_BD_liquidador_qa);
        int rs;
        try {
            conexion = conn.connect();
            Statement stmt = conexion.createStatement();
            rs = stmt.executeUpdate("select * from users where id = "+parametro+";");
            if (rs >0){
                System.out.println("Query exitosa");
            }else{
                System.out.println("La consulta no obtuvo resultados --> (0)");
            }
            stmt.close();
            conexion.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
