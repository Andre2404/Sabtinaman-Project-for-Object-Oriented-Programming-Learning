package model;

/**
 *
 * @author rizar
 */
public class Pengguna {
    private int idPengguna;
    private String nama;
    private String alamat;
    private String email;
    private int nomorKontak;
    private int saldo;
    private String password; // Tambahkan atribut password

    public Pengguna(int idPengguna, String nama, String alamat, String email, int nomorKontak, int saldo, String password) {
        this.idPengguna = idPengguna;
        this.nama = nama;
        this.alamat = alamat;
        this.email = email;
        this.nomorKontak = nomorKontak;
        this.saldo = saldo;
        this.password = password; // Simpan password langsung
    }

    // Getter dan Setter untuk password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdPengguna() {
        return idPengguna;
    }

    public void setIdPengguna(int idPengguna) {
        this.idPengguna = idPengguna;
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

    public int getSaldoPengguna() {
        return saldo;
    }

    public void setSaldoPengguna(int saldo) {
        this.saldo = saldo;
    }
}