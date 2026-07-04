/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package musicStudio.model;

public class Studio {

    int id;
    String name;
    int pricePerHour;
    String status;

    public Studio(
            int id,
            String name,
            int pricePerHour,
            String status) {

        this.id = id;
        this.name = name;
        this.pricePerHour = pricePerHour;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPricePerHour() {
        return pricePerHour;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return name;
    }
}