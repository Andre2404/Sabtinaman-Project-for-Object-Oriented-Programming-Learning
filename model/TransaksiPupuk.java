package model;

import java.sql.Date;
import java.time.LocalDate;

public class TransaksiPupuk {
    private int idTransaksi;
    private Pengguna user;
    private Pupuk pupuk;
    private int jumlahKg;
    private double totalHarga;
    private LocalDate tanggalBeli;

    // Constructor
    public TransaksiPupuk(int idTransaksi, Pengguna user, Pupuk pupuk, int jumlahKg, double totalHarga, LocalDate tanggalBeli) {
        this.idTransaksi = idTransaksi;
        this.user = user;
        this.pupuk = pupuk;
        this.jumlahKg = jumlahKg;
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

    public Pupuk getPupuk() {
        return pupuk;
    }

    public void setPupuk(Pupuk pupuk) {
        this.pupuk = pupuk;
    }

    public int getJumlahKg() {
        return jumlahKg;
    }

    public void setJumlahKg(int jumlahKg) {
        this.jumlahKg = jumlahKg;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(double totalHarga) {
        this.totalHarga = totalHarga;
    }

    public LocalDate getTanggalBeli() {
        return tanggalBeli;
    }

    public void setTanggalBeli(LocalDate tanggalBeli) {
        this.tanggalBeli = tanggalBeli;
    }

   
}
