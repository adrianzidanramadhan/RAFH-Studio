/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package musicStudio;

import java.util.HashMap;

public class Booking {
    Studio studio;
    int hours;

    HashMap<Instrument, Integer> instruments = new HashMap<>();

    public Booking(Studio studio, int hours) {
        this.studio = studio;
        this.hours = hours;
    }

    public void addInstrument(Instrument i) {
        instruments.put(i, instruments.getOrDefault(i, 0) + 1);
    }

    public void removeInstrument(Instrument i) {
        if (instruments.containsKey(i)) {
            int qty = instruments.get(i);
            if (qty > 1) {
                instruments.put(i, qty - 1);
            } else {
                instruments.remove(i);
            }
        }
    }

    public int getTotal() {
        int total = 0;

        if (studio != null) {
            total += studio.pricePerHour * hours;
        }

        for (Instrument i : instruments.keySet()) {
            total += i.price * instruments.get(i) * hours;
        }

        return total;
    }
}