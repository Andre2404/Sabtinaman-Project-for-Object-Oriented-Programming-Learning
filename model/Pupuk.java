package model;

public class Pupuk {
    private int idPupuk;
    private String namaPupuk;
    private double hargaPerKg;
    private int idPerusahaan;

    // Constructor
    public Pupuk(int idPupuk, String namaPupuk, double hargaPerKg, int idPerusahaan) {
        this.idPupuk = idPupuk;
        this.namaPupuk = namaPupuk;
        this.hargaPerKg = hargaPerKg;
        this.idPerusahaan = idPerusahaan;
    }

    // Getter dan Setter
    public int getIdPupuk() {
        return idPupuk;
    }

    public String getNamaPupuk() {
        return namaPupuk;
    }

    public double getHargaPerKg() {
        return hargaPerKg;
    }

    public int getIdPerusahaan() {
        return idPerusahaan;
    }

    public void setIdPupuk(int idPupuk) {
        this.idPupuk = idPupuk;
    }

    public void setNamaPupuk(String namaPupuk) {
        this.namaPupuk = namaPupuk;
    }

    public void setHargaPerKg(double hargaPerKg) {
        this.hargaPerKg = hargaPerKg;
    }

    public void setIdPerusahaan(int idPerusahaan) {
        this.idPerusahaan = idPerusahaan;
    }
}
