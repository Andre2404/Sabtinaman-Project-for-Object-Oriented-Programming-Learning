package model;

import java.sql.Timestamp;
import java.time.LocalDate;

public class TransaksiPupuk {
    private int idTransaksi;
    private Pengguna user;
    private Perusahaan company;
    private Pupuk pupuk;
    private String jenisPupuk;
    private int jumlahKg;
    private double totalHarga;
    private Timestamp tanggalBeli;
    private String tipeSaldo;

    // Constructor
    public TransaksiPupuk(int idTransaksi, Pengguna user, Perusahaan company, Pupuk pupuk, int jumlahKg, double totalHarga, Timestamp tanggalBeli, String tipeSaldo) {
        this.idTransaksi = idTransaksi;
        this.user = user;
        this.company = company;
        this.pupuk = pupuk;
        this.jumlahKg = jumlahKg;
        this.totalHarga = totalHarga;
        this.tanggalBeli = tanggalBeli;
        this.tipeSaldo = tipeSaldo;
        this.jenisPupuk = jenisPupuk;
    }

    public String getTipeSaldo() {
        return tipeSaldo;
    }

    public void setTipeSaldo(String tipeSaldo) {
        this.tipeSaldo = tipeSaldo;
    }

    public Perusahaan getCompany() {
        return company;
    }

    public void setCompany(Perusahaan company) {
        this.company = company;
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
    
    public String getJenisPupuk() {
        return jenisPupuk;
    }

    public void setJenisPupuk(String jenisPupuk) {
        this.jenisPupuk = jenisPupuk;
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

    public Timestamp getTanggalBeli() {
        return tanggalBeli;
    }

    public void setTanggalBeli(Timestamp tanggalBeli) {
        this.tanggalBeli = tanggalBeli;
    }

   
}
