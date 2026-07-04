package musicStudio.dao;

import musicStudio.config.DBConnection;
import java.sql.*;

public class BookingDAO {

    public static int insertBooking(
            int customerId,
            int studioId,
            Date tanggal,
            Time jamMulai,
            int durasi,
            int totalHarga) {

        int bookingId = -1;

        try {

            Connection conn =
                    DBConnection.getConnection();

            String sql =
                    "INSERT INTO booking "
                    + "(customer_id,studio_id,"
                    + "tanggal,jam_mulai,durasi,total_harga) "
                    + "VALUES(?,?,?,?,?,?)";

            PreparedStatement pst =
                    conn.prepareStatement(
                            sql,
                            Statement.RETURN_GENERATED_KEYS
                    );

            pst.setInt(1, customerId);
            pst.setInt(2, studioId);
            pst.setDate(3, tanggal);
            pst.setTime(4, jamMulai);
            pst.setInt(5, durasi);
            pst.setInt(6, totalHarga);

            pst.executeUpdate();

            ResultSet rs =
                    pst.getGeneratedKeys();

            if(rs.next()){
                bookingId = rs.getInt(1);
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        return bookingId;
    }
    public static boolean isBooked(
        int studioId,
        java.sql.Date tanggal,
        java.sql.Time jam) {

        try {

            Connection conn =
                    DBConnection.getConnection();

            String sql =
                "SELECT * FROM booking "
                + "WHERE studio_id=? "
                + "AND tanggal=? "
                + "AND jam_mulai=?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, studioId);
            ps.setDate(2, tanggal);
            ps.setTime(3, jam);

            ResultSet rs =
                    ps.executeQuery();

            return rs.next();

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public static int getTotalBooking() {

        try {

            Connection conn =
                    DBConnection.getConnection();

            String sql =
                    "SELECT COUNT(*) total FROM booking";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            if(rs.next()) {
                return rs.getInt("total");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    public static int getTotalRevenue() {

        try {

            Connection conn =
                    DBConnection.getConnection();

            String sql =
                    "SELECT SUM(total_harga) total FROM booking";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            if(rs.next()) {
                return rs.getInt("total");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}