package musicStudio;

public class TestCustomer {

    public static void main(String[] args) {

        int id =
                CustomerDAO.insert(
                        "Adrian",
                        "08123456789");

        System.out.println(
                "Customer ID = " + id);
    }
}