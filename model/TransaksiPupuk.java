package model;

import java.sql.Timestamp;
import java.time.LocalDate;

public class TransaksiPupuk {
    private int idTransaksi;
    private Pengguna user;
    private int totalHarga;
    private Timestamp tanggalBeli;

    // Constructor
    public TransaksiPupuk(int idTransaksi, Pengguna user, int totalHarga, Timestamp tanggalBeli) {
        this.idTransaksi = idTransaksi;
        this.user = user;
        this.totalHarga = totalHarga;
        this.tanggalBeli = tanggalBeli;
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

    
    
    public int getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(int totalHarga) {
        this.totalHarga = totalHarga;
    }

    public Timestamp getTanggalBeli() {
        return tanggalBeli;
    }

    public void setTanggalBeli(Timestamp tanggalBeli) {
        this.tanggalBeli = tanggalBeli;
    }

   
}
