package model;
import java.sql.Timestamp;

public class TransaksiSewa {
    private int idTransaksi;
    private Pengguna user;
    private Alat alat;
    private Timestamp tanggalSewa;

    // Constructor
    public TransaksiSewa(int idTransaksi, Pengguna user, Alat alat, Timestamp tanggalSewa) {
        this.idTransaksi = idTransaksi;
        this.user = user;
        this.alat = alat;
        this.tanggalSewa = tanggalSewa;
    }
    
    // Getters and Setters

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public Pengguna getUser() {
        return user;
    }

    public void setUser(Pengguna user) {
        this.user = user;
    }

    public Alat getAlat() {
        return alat;
    }

    public void setAlat(Alat alat) {
        this.alat = alat;
    }

    public Timestamp getTanggalSewa() {
        return tanggalSewa;
    }

    public void setTanggalSewa(Timestamp tanggalSewa) {
        this.tanggalSewa = tanggalSewa;
    }
    
}
