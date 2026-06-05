package musicStudio;

import java.sql.*;

public class CustomerDAO {

    public static int insert(
            String nama,
            String noHp) {

        int customerId = -1;

        try {

            Connection conn =
                    DBConnection.getConnection();

            String sql =
                    "INSERT INTO customer(nama,no_hp) VALUES(?,?)";

            PreparedStatement ps =
                    conn.prepareStatement(
                            sql,
                            Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, nama);
            ps.setString(2, noHp);

            ps.executeUpdate();

            ResultSet rs =
                    ps.getGeneratedKeys();

            if (rs.next()) {

                customerId = rs.getInt(1);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return customerId;
    }
}