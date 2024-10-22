package model;

public class Alat {
    private int idAlat;
    private String namaAlat;
    private String spesifikasi;
    private double hargaSewa;
    private int idPerusahaan;  // Alat disewakan oleh perusahaan

    public Alat(int idAlat, String namaAlat, int idPerusahaan, double hargaSewa) {
        this.idAlat = idAlat;
        this.namaAlat = namaAlat;
        this.spesifikasi = spesifikasi;
        this.hargaSewa = hargaSewa;
        this.idPerusahaan = idPerusahaan;
    }

    // Getters and Setters
    public int getIdAlat() {
        return idAlat;
    }

    public void setIdAlat(int idAlat) {
        this.idAlat = idAlat;
    }

    public String getNamaAlat() {
        return namaAlat;
    }

    public void setNamaAlat(String namaAlat) {
        this.namaAlat = namaAlat;
    }

    public String getSpesifikasi() {
        return spesifikasi;
    }

    public void setSpesifikasi(String spesifikasi) {
        this.spesifikasi = spesifikasi;
    }

    public double getHargaSewa() {
        return hargaSewa;
    }

    public void setHargaSewa(double hargaSewa) {
        this.hargaSewa = hargaSewa;
    }

    public int getIdPerusahaan() {
        return idPerusahaan;
    }

    public void setIdPerusahaan(int idPerusahaan) {
        this.idPerusahaan = idPerusahaan;
    }
}
