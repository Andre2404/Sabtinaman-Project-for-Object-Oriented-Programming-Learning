

package model;
import java.util.UUID;

public class Pupuk {
    private int idPupuk;
    private String namaPupuk;
    private double hargaPerKg;
    private Perusahaan company;

    // Constructor
    public Pupuk(int idPupuk, String namaPupuk, double hargaPerKg, Perusahaan company) {
        this.idPupuk = idPupuk;
        this.namaPupuk = namaPupuk;
        this.hargaPerKg = hargaPerKg;
        this.company = company;
    }
    
    // Getter dan Setter

    public int getIdPupuk() {
        return idPupuk;
    }

    public void setIdPupuk(int idPupuk) {
        this.idPupuk = idPupuk;
    }

    public String getNamaPupuk() {
        return namaPupuk;
    }

    public void setNamaPupuk(String namaPupuk) {
        this.namaPupuk = namaPupuk;
    }

    public double getHargaPerKg() {
        return hargaPerKg;
    }

    public void setHargaPerKg(double hargaPerKg) {
        this.hargaPerKg = hargaPerKg;
    }

    public Perusahaan getCompany() {
        return company;
    }

    public void setCompany(Perusahaan company) {
        this.company = company;
    }
   
}
