package musicStudio.dao;

import musicStudio.config.DBConnection;
import java.sql.*;

public class AdminDAO {

    public static boolean login(
            String username,
            String password) {

        try {

            Connection conn =
                    DBConnection.getConnection();

            String sql =
                    "SELECT * FROM admin "
                    + "WHERE username=? "
                    + "AND password=?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs =
                    ps.executeQuery();

            return rs.next();

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}