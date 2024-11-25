package model;

public class Alat {
    private int idAlat;
    private String namaAlat;
    private String spesifikasi;
    private int hargaSewa;
    private Perusahaan company;  // Alat disewakan oleh perusahaan

    public Alat(int idAlat, String namaAlat, String spesifikasi, Perusahaan company, int hargaSewa) {
        this.idAlat = idAlat;
        this.namaAlat = namaAlat;
        this.spesifikasi = spesifikasi;
        this.hargaSewa = hargaSewa;
        this.company = company;
  
    }
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

    public int getHargaSewa() {
        return hargaSewa;
    }

    public void setHargaSewa(int hargaSewa) {
        this.hargaSewa = hargaSewa;
    }

    public Perusahaan getCompany() {
        return company;
    }

    public void setCompany(Perusahaan company) {
        this.company = company;
    }

    
}
    
