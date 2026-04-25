/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package musicStudio;

public class Instrument {
    String name;
    int price;

    public Instrument(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String toString() {
        return name;
    }
}