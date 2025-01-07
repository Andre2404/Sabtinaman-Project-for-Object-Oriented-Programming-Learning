package model;
import java.time.LocalDate;

public class TransaksiSewa {
    private int idTransaksi;
    private Pengguna user;
    private int totalHarga;
    private LocalDate tanggalTransaksi;

    // Constructor
    public TransaksiSewa(int idTransaksi, Pengguna user, int totalHarga, LocalDate tanggalTransaksi) {
        this.idTransaksi = idTransaksi;
        this.user = user;
        this.totalHarga = totalHarga;
        this.tanggalTransaksi = tanggalTransaksi;
    }

    public LocalDate getTanggalTransaksi() {
        return tanggalTransaksi;
    }

    public void setTanggalTransaksi(LocalDate tanggalTransaksi) {
        this.tanggalTransaksi = tanggalTransaksi;
    }

    
    public int getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(int totalHarga) {
        this.totalHarga = totalHarga;
    }

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
   
}
