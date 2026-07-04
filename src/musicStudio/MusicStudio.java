package musicStudio;

import musicStudio.dao.StudioDAO;
import musicStudio.model.Studio;

public class TestStudioDAO {

    public static void main(String[] args) {

        System.out.println("=== DATA STUDIO ===");

        for (Studio s : StudioDAO.getAllStudio()) {

            System.out.println(
                    s.getId() + " | " +
                    s.getName() + " | " +
                    s.getPricePerHour() + " | " +
                    s.getStatus()
            );

        }
    }
}