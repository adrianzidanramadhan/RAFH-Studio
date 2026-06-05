package musicStudio;



import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {

        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            System.out.println("DATABASE CONNECTED");
        } else {
            System.out.println("DATABASE FAILED");
        }
    }
}