package musicStudio.config;



import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/rafh_studio";

    private static final String USER =
            "root";

    private static final String PASSWORD =
            "";

    public static Connection getConnection() {

        try {

            Connection conn = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

            return conn;

        } catch (Exception e) {

            System.out.println("Koneksi gagal!");
            e.printStackTrace();

            return null;
        }
    }
}