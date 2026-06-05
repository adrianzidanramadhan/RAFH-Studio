package musicStudio;

import java.sql.*;
import java.util.ArrayList;

public class BookingHistoryDAO {

    public static ArrayList<String> getHistory() {

        ArrayList<String> list =
                new ArrayList<>();

        try {

            Connection conn =
                    DBConnection.getConnection();

            String sql =
                    "SELECT b.id,"
                    + "s.nama AS studio,"
                    + "b.tanggal,"
                    + "b.jam_mulai,"
                    + "b.durasi,"
                    + "b.total_harga "
                    + "FROM booking b "
                    + "JOIN studio s "
                    + "ON b.studio_id=s.id "
                    + "ORDER BY b.id DESC";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while(rs.next()) {

                String row =
                        rs.getInt("id")
                        + " | "
                        + rs.getString("studio")
                        + " | "
                        + rs.getDate("tanggal")
                        + " | "
                        + rs.getTime("jam_mulai")
                        + " | "
                        + rs.getInt("durasi")
                        + " Jam | Rp "
                        + rs.getInt("total_harga");

                list.add(row);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}