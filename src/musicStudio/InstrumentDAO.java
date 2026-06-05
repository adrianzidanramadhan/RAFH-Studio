package musicStudio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class InstrumentDAO {

    public static ArrayList<Instrument> getAllInstrument() {

        ArrayList<Instrument> list = new ArrayList<>();

        try {

            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM instrument";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Instrument instrument = new Instrument(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getInt("harga"),
                        rs.getInt("stok")
                );

                list.add(instrument);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
    public static void insert(String nama, int harga, int stok) {

        try {

            Connection conn = DBConnection.getConnection();

            String sql =
                    "INSERT INTO instrument(nama,harga,stok) VALUES(?,?,?)";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1, nama);
            ps.setInt(2, harga);
            ps.setInt(3, stok);

            ps.executeUpdate();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void delete(int id) {

        try {

            Connection conn = DBConnection.getConnection();

            String sql =
                    "DELETE FROM instrument WHERE id=?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void update(
        int id,
        String nama,
        int harga,
        int stok) {

        try {

            Connection conn = DBConnection.getConnection();

            String sql =
                    "UPDATE instrument SET nama=?, harga=?, stok=? WHERE id=?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1, nama);
            ps.setInt(2, harga);
            ps.setInt(3, stok);
            ps.setInt(4, id);

            ps.executeUpdate();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void reduceStock(
        int instrumentId,
        int qty) {

        try {

            Connection conn =
                    DBConnection.getConnection();

            String sql =
                    "UPDATE instrument "
                    + "SET stok = stok - ? "
                    + "WHERE id=?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, qty);
            ps.setInt(2, instrumentId);

            ps.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}