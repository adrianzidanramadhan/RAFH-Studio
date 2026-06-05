package musicStudio;

public class Customer {

    int id;
    String nama;
    String noHp;

    public Customer(
            int id,
            String nama,
            String noHp) {

        this.id = id;
        this.nama = nama;
        this.noHp = noHp;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getNoHp() {
        return noHp;
    }
}