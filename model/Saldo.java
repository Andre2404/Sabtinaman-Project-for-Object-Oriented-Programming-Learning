package model;
import java.util.UUID;
import java.sql.Timestamp;

public class Saldo {
    private int idSaldo;
    private Pengguna user;
    private Perusahaan company;
    private double jumlah;
    private String tipeSaldo;
    private Timestamp tanggalTransaksi;

   public Saldo(int idSaldo, Pengguna user, Perusahaan company, double jumlah, String tipeSaldo, Timestamp tanggalTransaksi) {
        this.idSaldo = idSaldo;
        this.user = user;
        this.company = company;
        this.jumlah = jumlah;
        this.tipeSaldo = tipeSaldo;
        this.tanggalTransaksi = tanggalTransaksi;
    }
    
    public int getIdSaldo() {
        return idSaldo;
    }

    public void setIdSaldo(int idSaldo) {
        this.idSaldo = idSaldo;
    }

    public Pengguna getUser() {
        return user;
    }

    public void setUser(Pengguna user) {
        this.user = user;
    }

    public Perusahaan getCompany() {
        return company;
    }

    public void setCompany(Perusahaan company) {
        this.company = company;
    }

    public double getJumlah() {
        return jumlah;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

    public String getTipeSaldo() {
        return tipeSaldo;
    }

    public void setTipeSaldo(String tipeSaldo) {
        this.tipeSaldo = tipeSaldo;
    }

    public Timestamp getTanggalTransaksi() {
        return tanggalTransaksi;
    }

    public void setTanggalTransaksi(Timestamp tanggalTransaksi) {
        this.tanggalTransaksi = tanggalTransaksi;
    }

   
}
