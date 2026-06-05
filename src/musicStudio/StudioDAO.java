package musicStudio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class StudioDAO {

    // =====================
    // GET ALL STUDIO
    // =====================
    public static ArrayList<Studio> getAllStudio() {

        ArrayList<Studio> list = new ArrayList<>();

        try {

            Connection conn =
                    DBConnection.getConnection();

            String sql =
                    "SELECT * FROM studio";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                Studio studio =
                        new Studio(
                                rs.getInt("id"),
                                rs.getString("nama"),
                                rs.getInt("harga_per_jam"),
                                rs.getString("status")
                        );

                list.add(studio);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =====================
    // INSERT STUDIO
    // =====================
    public static void insert(
            String nama,
            int harga,
            String status) {

        try {

            Connection conn =
                    DBConnection.getConnection();

            String sql =
                    "INSERT INTO studio(nama,harga_per_jam,status) VALUES(?,?,?)";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1, nama);
            ps.setInt(2, harga);
            ps.setString(3, status);

            ps.executeUpdate();

            System.out.println("Studio berhasil ditambahkan");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================
    // UPDATE STUDIO
    // =====================
    public static void update(
            int id,
            String nama,
            int harga,
            String status) {

        try {

            Connection conn =
                    DBConnection.getConnection();

            String sql =
                    "UPDATE studio SET nama=?, harga_per_jam=?, status=? WHERE id=?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setString(1, nama);
            ps.setInt(2, harga);
            ps.setString(3, status);
            ps.setInt(4, id);

            ps.executeUpdate();

            System.out.println("Studio berhasil diupdate");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================
    // DELETE STUDIO
    // =====================
    public static void delete(int id) {

        try {

            Connection conn =
                    DBConnection.getConnection();

            String sql =
                    "DELETE FROM studio WHERE id=?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();

            System.out.println("Studio berhasil dihapus");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}