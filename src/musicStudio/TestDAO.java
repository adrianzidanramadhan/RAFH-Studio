package musicStudio;

import java.util.ArrayList;

public class TestDAO {

    public static void main(String[] args) {

        ArrayList<Instrument> instruments =
                InstrumentDAO.getAllInstrument();

        for (Instrument i : instruments) {

            System.out.println(
                    i.getId() + " | " +
                    i.getName() + " | " +
                    i.getPrice() + " | " +
                    i.getStock()
            );

        }
    }
}