package musicStudio.dao;

import musicStudio.config.DBConnection;
import java.sql.*;

public class BookingDetailDAO {

    public static void insertDetail(
            int bookingId,
            int instrumentId,
            int qty) {

        try {

            Connection conn =
                    DBConnection.getConnection();

            String sql =
                    "INSERT INTO booking_detail "
                    + "(booking_id,instrument_id,qty)"
                    + " VALUES(?,?,?)";

            PreparedStatement pst =
                    conn.prepareStatement(sql);

            pst.setInt(1, bookingId);
            pst.setInt(2, instrumentId);
            pst.setInt(3, qty);

            pst.executeUpdate();

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}