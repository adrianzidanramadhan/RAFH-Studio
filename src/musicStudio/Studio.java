/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package musicStudio;

public class Studio {
    String name;
    int pricePerHour;

    public Studio(String name, int pricePerHour) {
        this.name = name;
        this.pricePerHour = pricePerHour;
    }

    public String toString() {
        return name + " - Rp" + pricePerHour + "/jam";
    }
}