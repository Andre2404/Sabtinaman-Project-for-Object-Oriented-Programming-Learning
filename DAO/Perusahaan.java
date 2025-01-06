package model;
/**
 *
 * @author rizar
 */
public class Perusahaan {
    private int idPerusahaan;
    private String nama;
    private String alamat;
    private String email;
    private int nomorKontak;
    private int saldoPerusahaan;
    private String password; // Tambahkan atribut password

     public Perusahaan(int idPerusahaan, String nama, String alamat, String email, int nomorKontak, int saldoPerusahaan, String password) {
        this.idPerusahaan = idPerusahaan;
        this.nama = nama;
        this.alamat = alamat;
        this.email = email;
        this.nomorKontak = nomorKontak;
        this.saldoPerusahaan = saldoPerusahaan;
        this.password = password; // Simpan password langsung
    }
     
     public Perusahaan(int idPerusahaan, String nama, String alamat, String email, int nomorKontak, int saldoPerusahaan) {
        this.idPerusahaan = idPerusahaan;
        this.nama = nama;
        this.alamat = alamat;
        this.email = email;
        this.nomorKontak = nomorKontak;
        this.saldoPerusahaan = saldoPerusahaan;
     }
    // Getter dan Setter untuk password

    public int getIdPerusahaan() {
        return idPerusahaan;
    }

    public void setIdPerusahaan(int idPerusahaan) {
        this.idPerusahaan = idPerusahaan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNomorKontak() {
        return nomorKontak;
    }

    public void setNomorKontak(int nomorKontak) {
        this.nomorKontak = nomorKontak;
    }

    public int getSaldoPerusahaan() {
        return saldoPerusahaan;
    }

    public void setSaldoPerusahaan(int saldoPerusahaan) {
        this.saldoPerusahaan = saldoPerusahaan;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
   
}