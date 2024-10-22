package model;
import java.sql.Timestamp;

public class TransaksiSewa {
    private int idTransaksi;
    private int idPengguna;
    private int idAlat;
    private Timestamp tanggalSewa;

    // Constructor
    public TransaksiSewa(int idTransaksi, int idPengguna, int idAlat, Timestamp tanggalSewa) {
        this.idTransaksi = idTransaksi;
        this.idPengguna = idPengguna;
        this.idAlat = idAlat;
        this.tanggalSewa = tanggalSewa;
    }

    // Getters and Setters
    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public int getIdPengguna() {
        return idPengguna;
    }

    public void setIdPengguna(int idPengguna) {
        this.idPengguna = idPengguna;
    }

    public int getIdAlat() {
        return idAlat;
    }

    public void setIdAlat(int idAlat) {
        this.idAlat = idAlat;
    }

    public Timestamp getTanggalSewa() {
        return tanggalSewa;
    }

    public void setTanggalSewa(Timestamp tanggalSewa) {
        this.tanggalSewa = tanggalSewa;
    }
}
