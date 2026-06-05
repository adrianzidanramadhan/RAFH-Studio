package musicStudio;

public class TestBooking {

    public static void main(String[] args) {

        int bookingId =
                BookingDAO.insert(
                        1,
                        1,
                        "2026-06-05",
                        "19:00:00",
                        2,
                        150000);

        System.out.println(
                "BOOKING ID = "
                + bookingId);
    }
}